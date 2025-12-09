package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.google.firebase.firestore.FirebaseFirestore

class MedicoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val medicoCollection= db.collection("Medico")

    fun obtenerMedicoPorUsuario(idUsuario: String, callback:(Medico?) -> Unit) {
        medicoCollection
            .whereEqualTo("Id_usuario", idUsuario)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                val medico= doc?.toObject(Medico::class.java)
                callback(medico)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}