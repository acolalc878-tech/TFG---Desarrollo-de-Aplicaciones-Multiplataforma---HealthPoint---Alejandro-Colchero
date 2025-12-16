package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistrerScreenViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val isLoading = mutableStateOf(false)
    val lastError = mutableStateOf<String?>(null)

    fun registerUser(
        nombre: String,
        apellidos: String,
        edad: Int,
        email: String,
        telefono: String,
        password: String,
        role: String, // "Paciente" o "Medico"
        fechaNacimiento: String,
        idCentroMedico: String? = null,
        especialidad: String = "",
        numColegiado: String = "",
        horario: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            var authUID: String? = null
            try {
                isLoading.value = true
                lastError.value = null

                // Crear usuario en FirebaseAuth
                val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()

                // Generar un id de usuario personalizado
                authUID = result.user?.uid

                if (authUID.isNullOrBlank()) {
                    throw IllegalStateException("El UID de Firebase Auth está vacío.")
                }

                // Creamos el documento con campos iguales a los del modelo de tablas
                val objUsuario = hashMapOf(
                    "Id_usuario" to authUID,
                    "nombre" to nombre,
                    "apellido" to apellidos,
                    "edad" to edad,
                    "email" to email.trim(),
                    "telefono" to telefono,
                    "contraseña" to password,
                    "rol" to role,
                    "fechaNacimiento" to fechaNacimiento
                )

                db.collection("Usuario").document(authUID).set(objUsuario).await()

                // Si es un medico, creamos el documento Medico con su propio id
                if (role == "Medico") {
                    // Generamos el id
                    val objMedico = hashMapOf(
                        "Id_medico" to authUID,
                        "nombre" to nombre,
                        "apellido" to apellidos,
                        "Id_usuario" to authUID,
                        "Id_centroMedico" to (idCentroMedico ?: ""),
                        "especialidad" to especialidad,
                        "numColegiado" to numColegiado,
                        "horario" to horario
                    )
                    db.collection("Medico").document(authUID).set(objMedico).await()
                }

                if(role == "Paciente") {
                    val paciente = hashMapOf(
                        "Id_paciente" to authUID,
                        "nombre" to nombre,
                        "apellido" to apellidos,
                        "Id_usuario" to authUID,
                    )
                    db.collection("Paciente").document(authUID).set(paciente).await()
                }

                // El usuario debe loguearse al crear la cuenta
                auth.signOut()

                isLoading.value = false
                onSuccess()

            } catch (e: Exception) {
                try{
                    auth.currentUser?.delete()
                } catch (e: Exception){}

                lastError.value = e.message
                isLoading.value = false
                onError(e.message ?: "Error al registrar el/la usuari@")
            }
        }
    }
}