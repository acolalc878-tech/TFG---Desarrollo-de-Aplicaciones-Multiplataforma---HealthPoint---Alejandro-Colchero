package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitasRepository {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("Cita")

    suspend fun obtenerCitasPorMedico(idMedico: String): List<Cita> {
        val snap = coleccion
            .whereEqualTo("id_medico", idMedico)
            .get()
            .await()

        return snap.documents.mapNotNull { it.toObject(Cita::class.java) }
    }
}