package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val citasReferencia = db.collection("Cita")

    fun obtenerCitasPorMedico(idMedico: String, callback: (List<Cita>) -> Unit) {
        db.collection("Cita")
            .whereEqualTo("Id_medico", idMedico)
            .get()
            .addOnSuccessListener { result ->
                callback(result.toObjects(Cita::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }
}