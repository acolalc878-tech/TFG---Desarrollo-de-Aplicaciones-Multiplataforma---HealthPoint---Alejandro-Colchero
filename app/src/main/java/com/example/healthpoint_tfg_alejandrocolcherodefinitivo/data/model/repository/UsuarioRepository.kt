package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun obtenerTodosLosUsuarios() : List<Usuario> {
        val snap = db.collection("Usuario")
            .whereEqualTo("rol", "Paciente")
            .get()
            .await()

        return snap.documents.mapNotNull { it.toObject(Usuario::class.java) }
    }
}