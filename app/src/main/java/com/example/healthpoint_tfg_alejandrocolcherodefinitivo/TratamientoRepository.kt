package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore

class TratamientoRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerTratamientoPorCita(idCita: String, callback: (List<Tratamiento>) -> Unit){
        db.collection("Tratamiento").whereEqualTo("id_cita", idCita)
            .get().addOnSuccessListener { result ->
                callback(result.toObjects(Tratamiento::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }
}