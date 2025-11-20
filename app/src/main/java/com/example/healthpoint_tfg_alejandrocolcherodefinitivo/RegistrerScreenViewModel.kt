package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class RegistrerScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Funcion suspendida para generar el id del usuario usando un contador
    private suspend fun generarIdUsuario(collectionName: String, prefix: String, digits: Int = 3): String {
        val snapshot = db.collection(collectionName).get().await()
        val next = snapshot.size() + 1
        return prefix + next.toString().padStart(digits, '0')
    }

    val isLoading = mutableStateOf(false)

    // Función auxiliar para eliminar datos si el guardado secundario falla
    private fun deleteInconsistentUserData(userId: String) {
        // Elimina la cuenta de Firebase Authentication
        auth.currentUser?.delete()
        // Elimina el documento ya creado en la colección 'Usuario'
        db.collection("Usuario").document(userId).delete()
    }

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
            try{
                // Crear en FirebaseAuth
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                // Generar un id de usuario con el formato del contador
                val nuevoIdUsuario = generarIdUsuario("Usuario", "user", 3)

                // Creamos el documento con campos iguales a los del modelo de tablas
                val objUsuario = hashMapOf(
                    "Id_usuario" to nuevoIdUsuario,
                    "nombre" to nombre,
                    "apellido" to apellidos,
                    "edad" to edad,
                    "email" to email,
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
                        "Id_centroMedico" to (idCentroMedico ?: ""), // Puede estar vacío si no se asigna
                        "especialidad" to especialidad,
                        "numColegiado" to numColegiado,
                        "horario" to horario
                    )

                    db.collection("Medico").document(nuevoIdMedico).set(objMedico).await()
                }

                onSuccess()
            } catch (e:Exception) {
                onError(e.message ?: "Error al registrar el/la usuari@")
            }
        }

        isLoading.value = true
        val cleanedEmail = email.trim()

        auth.createUserWithEmailAndPassword(cleanedEmail, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener run {
                        isLoading.value = false
                        onError("No se pudo obtener el UID del usuario")
                    }

                    // Asegúrate de NO guardar la contraseña en Firestore
                    val user = hashMapOf(
                        "Id_usuario" to userId,
                        "nombre" to nombre,
                        "apellidos" to apellidos,
                        "email" to cleanedEmail, // Usamos el email limpio
                        "telefono" to telefono,
                        "rol" to role,
                        "fechaNacimiento" to fechaNacimiento
                    )

                    // 2. Primer Guardado: Colección "Usuario" (Siempre)
                    db.collection("Usuario").document(userId).set(user)
                        .addOnSuccessListener {
                            // 3. Segundo Guardado: Colección específica (Condicional)
                            if(role == "Medico") {
                                // Guardar en "Medico"
                                val medico = hashMapOf(
                                    "Id_Medico" to userId,
                                    "nombre" to nombre,
                                    "apellidos" to apellidos,
                                    "Id_usuario" to userId,
                                    "especialidad" to especialidad,
                                    "numColegiado" to numColegiado,
                                    "horario" to horario
                                )
                                db.collection("Medico").document(userId).set(medico)
                                    .addOnSuccessListener {
                                        isLoading.value = false
                                        onSuccess() // Éxito en ambas colecciones
                                    }

                                    .addOnFailureListener { e ->
                                        // Falló en Medico. Limpiar datos inconsistentes.
                                        deleteInconsistentUserData(userId)
                                        isLoading.value = false
                                        onError(e.message ?: "Error al guardar los datos de Medico")
                                    }

                            } else if (role == "Paciente") {
                                // ¡Lógica de Paciente AÑADIDA correctamente!
                                // Usa el mismo formato de datos que el documento 'Usuario'
                                val paciente = hashMapOf(
                                    "Id_Paciente" to userId,
                                    "nombre" to nombre,
                                    "apellidos" to apellidos,
                                    "Id_usuario" to userId,
                                    "email" to cleanedEmail,
                                    "rol" to role,
                                    "telefono" to telefono,
                                    "fechaNacimiento" to fechaNacimiento
                                )
                                db.collection("Paciente").document(userId).set(paciente)
                                    .addOnSuccessListener {
                                        isLoading.value = false
                                        onSuccess() // Éxito en ambas colecciones
                                    }
                                    .addOnFailureListener { e ->
                                        // Falló en Paciente. Limpiar datos inconsistentes.
                                        deleteInconsistentUserData(userId)
                                        isLoading.value = false
                                        onError(e.message ?: "Error al guardar los datos de Paciente")
                                    }
                            } else {
                                // Rol desconocido (solo guardado en Usuario)
                                isLoading.value = false
                                onSuccess()
                            }
                        }

                        .addOnFailureListener { e ->
                            // Falló la escritura en Usuario. Limpiar solo Auth.
                            auth.currentUser?.delete()
                            isLoading.value = false
                            onError(e.message ?: "Error al guardar en la colección Usuario")
                        }
                } else {
                    isLoading.value = false
                    // Error de autenticación (contraseña débil, email ya existe, o mal formato)
                    onError(task.exception?.message ?: "Error al crear usuario de autenticación")
                }
            }
    }
}