package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore

class MedicamentoRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerMedicamentosPorTratamiento(idTratamiento: String, callback:(List<Medicamento>) -> Unit) {
        db.collection("Medicamento").whereEqualTo("id_tratamiento", idTratamiento)
            .get().addOnSuccessListener { result ->
                callback(result.toObjects(Medicamento::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }

}