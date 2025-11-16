package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Error

class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Indicamos si la operacion de carga esta en curso
    private val _loading = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = _loading

    // Rol seleccionado por el usuario
    val selectedRole = MutableLiveData("Paciente") // Medico o Paciente
    // Mensaje de error en la interfaz si falla el login
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: MutableLiveData<String?> = _errorMessage


    fun selectedRole(role: String){
        errorMessage.value
        selectedRole.value = role
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (String) -> Unit, // Devolvemos el rol Medico o Pacinte
        onError: (String) -> Unit
    ) {
        _errorMessage.value = null
        _loading.value = true

        viewModelScope.launch {
            try {
                if ( email.isBlank() || password.isBlank() ) {
                    throw IllegalArgumentException("El correo o la contraseña no pueden estar vacíos")
                }

                auth.signInWithEmailAndPassword(email, password).await()

                val userId = auth.currentUser?.uid
                if(userId == null){
                    throw Exception("No se pudo obtener el Id del usuario")
                }

                // Consultamos la BBDD para saber que rol tiene asignado el usuario
                val snapshot = db.collection("Usuario").document(userId).get().await()
                if(!snapshot.exists()){
                    throw Exception("No se encontróel usuario en Firestore")
                }

                val role = snapshot.getString("rol") ?: "Paciente"
                Log.d("LOGIN", "Usuario con rol: $role ")

                // Devolvemos el rol para navegar a la pantalla que corresponda segun el rol del usuario
                onLoginSuccess(role)

            } catch (e: Exception) {
                Log.d("ERROR_LOGIN", e.message ?: "Error desconocido")
                _errorMessage.value = e.message
                onError(e.message ?: "Error al iniciar sesión")
            } finally {
                // Desactivamos el cargado siempre
                _loading.value = false
            }
        }
    }

}