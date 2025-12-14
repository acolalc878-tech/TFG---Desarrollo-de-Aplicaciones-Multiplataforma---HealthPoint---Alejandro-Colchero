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

    // Funcion suspendida para generar el id del usuario usando un contador
    private suspend fun generarIdUsuario(collectionName: String, prefijo: String, digits: Int = 3): String {
        val snapshot = db.collection(collectionName).get().await()
        val next = snapshot.size() + 1
        return prefijo + next.toString().padStart(digits, '0')
    }

    /**
     * Generamos un usuario en FireAuth para poder hacer el login mas tarde
     *
     * Generamos un id interno con el siguiente formato "userNNN" y lo guardamos en /Usuario/{userNNN}
     *
     * Si es médico lo creamos en /Medico/{medNNN} haciendo referencia al id del usuario que es = a userNNN
     *
     * No dejamos documentos duplicados
     *
     * Hacemos que cuando se cree una cuenta, salga hacia el login para logearse, sin que inicie sesion directamente
     * al crear el usuario
     */

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
            try {
                isLoading.value = true
                lastError.value = null

                // Crear usuario en FirebaseAuth
                auth.createUserWithEmailAndPassword(email.trim(), password).await()

                // Generar un id de usuario personalizado
                val nuevoIdUsuario = generarIdUsuario("Usuario", "user", 3)

                // Creamos el documento con campos iguales a los del modelo de tablas
                val objUsuario = hashMapOf(
                    "Id_usuario" to nuevoIdUsuario,
                    "nombre" to nombre,
                    "apellido" to apellidos,
                    "edad" to edad,
                    "email" to email.trim(),
                    "telefono" to telefono,
                    "contraseña" to password,
                    "rol" to role,
                    "fechaNacimiento" to fechaNacimiento
                )

                db.collection("Usuario").document(nuevoIdUsuario).set(objUsuario).await()

                // Si es un medico, creamos el documento Medico con su propio id
                if (role == "Medico") {
                    // Generamos el id
                    val nuevoIdMedico = generarIdUsuario("Medico", "med", 3)
                    val objMedico = hashMapOf(
                        "Id_medico" to nuevoIdMedico,
                        "nombre" to nombre,
                        "apellido" to apellidos,
                        "Id_usuario" to nuevoIdUsuario,
                        "Id_centroMedico" to (idCentroMedico
                            ?: ""), // Puede estar vacío si no se asigna
                        "especialidad" to especialidad,
                        "numColegiado" to numColegiado,
                        "horario" to horario
                    )
                    db.collection("Medico").document(nuevoIdMedico).set(objMedico).await()
                }

                if(role == "Paciente") {
                    val idPaciente = generarIdUsuario("Paciente", "pac")
                    val paciente = hashMapOf(
                        "Id_paciente" to idPaciente,
                        "nombre" to nombre,
                        "apellido" to apellidos,
                        "Id_usuario" to nuevoIdUsuario,
                        "especialidad" to especialidad,
                        "numColegiado" to numColegiado,
                        "horario" to horario
                    )
                    db.collection("Paciente").document(idPaciente).set(paciente).await()
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