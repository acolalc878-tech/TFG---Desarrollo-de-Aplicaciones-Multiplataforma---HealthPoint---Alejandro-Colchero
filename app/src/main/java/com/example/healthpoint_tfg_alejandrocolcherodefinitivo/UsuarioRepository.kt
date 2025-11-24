package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository {

    // Hacemos referencia a la base de datos y a los usuarios
    private val db = FirebaseFirestore.getInstance()
    private val userReferencia = db.collection("Usuario")

    // Hacemos la busqueda de pacientes por nombre
    suspend fun buscarPacientesPorNombre(nombre: String): List<Usuario> {
        val snapshot = userReferencia.whereEqualTo("rol", "Paciente")
            .whereGreaterThanOrEqualTo("nombre", nombre)
            .whereLessThanOrEqualTo("nombre", nombre + "\uf8ff")
            .get()
            .await()

        return snapshot.toObjects(Usuario::class.java)
    }

    // Hacemos la busqueda de usuario por email también
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        val snapshot = userReferencia.whereEqualTo("email", email).get().await()
        return snapshot.documents.firstOrNull()?.toObject(Usuario::class.java)
    }
}