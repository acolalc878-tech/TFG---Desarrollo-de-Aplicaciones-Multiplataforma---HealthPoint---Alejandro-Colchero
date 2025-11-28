package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()

                // Buscamos el usuario por el email
                val snapshot = db.collection("Usuario")
                    .whereEqualTo("email", email.trim())
                    .limit(1)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    // Si no hay un documento devolvermos error
                    auth.signOut()
                    onError("No se ha encontrado el usuario en la base de datos")
                    return@launch
                }

                val document = snapshot.documents[0]

                val role = document.getString("rol") ?: ""
                val idUsuario = document.getString("Id_usuario") ?: ""
                if (role.isBlank()){
                    auth.signOut()
                    onError("No se encontró el rol de usuario en la base de dato")
                    return@launch
                }

                if (idUsuario.isBlank()){
                    auth.signOut()
                    onError("El usuario no tiene id")
                    return@launch
                }

                onLoginSuccess(role, idUsuario)
            } catch (e: Exception){
                onError(e.message ?: "Error al inicar sesion")
            }
        }
    }
}