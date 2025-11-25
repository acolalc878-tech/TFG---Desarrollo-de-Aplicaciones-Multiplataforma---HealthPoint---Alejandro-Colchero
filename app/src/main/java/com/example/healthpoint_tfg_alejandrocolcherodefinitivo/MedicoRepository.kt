package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore

class MedicoRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerMedicoPorUsuario(idUsuario: String, callback:(Medico?) -> Unit) {
        db.collection("Medico").whereEqualTo("id_usuario", idUsuario)
            .get().addOnSuccessListener { result ->
                callback(result.documents.firstOrNull()?.toObject(Medico::class.java))
            }
            .addOnFailureListener { callback(null) }
    }

    fun obtenerCentroMedico(idCentroMedico: String, callback:(CentroMedico?) -> Unit) {
        db.collection("CentroMedico")
            .document(idCentroMedico).get()
            .addOnSuccessListener { document ->
                callback(document.toObject(CentroMedico::class.java))
            }
            .addOnFailureListener { callback(null) }
    }
}