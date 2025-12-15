package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository {

    // Instancia a Firestore
    private val db = FirebaseFirestore.getInstance()

    // Funcion que devuelve una lista con todos los usuarios con el rol de paciente
    suspend fun obtenerTodosLosUsuarios() : List<Usuario> {
        // Consulta con filtro donde se buscan a todos los usuarios con el rol de Paciente
        val snap = db.collection("Usuario")
            .whereEqualTo("rol", "Paciente")
            .get()
            .await()

        return snap.documents.mapNotNull { it.toObject(Usuario::class.java) }
    }
}