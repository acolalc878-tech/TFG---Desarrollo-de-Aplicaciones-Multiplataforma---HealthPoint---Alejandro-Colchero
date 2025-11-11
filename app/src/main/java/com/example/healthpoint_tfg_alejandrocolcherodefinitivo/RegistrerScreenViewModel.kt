package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date

class RegistrerScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val isLoading = mutableStateOf(false)

    fun registrerUser(
        nombre: String,
        apellidos: String,
        email: String,
        telefono: String,
        password: String,
        role: String,
        fechaNacimiento: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val user = hashMapOf(
                        "Id_usuario" to uid,
                        "nombre" to nombre,
                        "apellidos" to apellidos,
                        "email" to email,
                        "telefono" to telefono,
                        "contraseña" to password,
                        "rol" to role,
                        "fechaNacimiento" to fechaNacimiento
                    )

                    // Guardamos en la coleccion "Usuario"
                    db.collection("Usuario").document(uid).set(user)
                        .addOnSuccessListener {
                            if(role == "Medico") {
                                // Si es medico, se guarda en la coleccion de "Medico"
                                val medico = hashMapOf(
                                    "Id_Medico" to uid,
                                    "nombre" to nombre,
                                    "apellidos" to apellidos,
                                    "Id_usuario" to uid,
                                    "especialidad" to "",
                                    "numColegiado" to "",
                                    "horario" to ""
                                )

                                db.collection("Medico").document(uid).set(medico)
                                    .addOnSuccessListener {
                                        isLoading.value = false
                                        onSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading.value = false
                                        onError(e.message ?: "Error al guardar el médico")
                                    }
                            } else {
                                // Si es paciente, se guarda en la coleccion de "Paciente"
                                isLoading.value = false
                                onSuccess()
                            }
                        }
                        .addOnFailureListener { e ->
                            isLoading.value = false
                            onError(e.message ?: "Error desconocido")
                        }

                    val collection = if (role == "Paciente") "Pacientes" else "Medicos"
                    db.collection(collection).document(uid).set(user)
                        .addOnSuccessListener {
                            isLoading.value = false
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            isLoading.value = false
                            onError(e.message ?: "Error al guardar en la base de datos")
                        }
                } else {
                    isLoading.value = false
                    onError(task.exception?.message ?: "Error desconocido")
                }
            }
    }
}