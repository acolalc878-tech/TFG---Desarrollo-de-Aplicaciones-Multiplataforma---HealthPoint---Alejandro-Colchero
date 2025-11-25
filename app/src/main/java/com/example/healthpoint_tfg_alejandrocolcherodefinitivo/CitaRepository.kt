package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class CitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val citasReferencia = db.collection("Cita")

    // Creamos Cita con corrutinas
    suspend fun crearCita(cita: Cita): String {
        val docRef = citasReferencia.document()
        val citaConId = cita.copy(Id_Cita = docRef.id)
        docRef.set(citaConId).await()
        return docRef.id
    }

    // Para editar la cita con corrutinas
    suspend fun editarCira(cita: Cita){
        citasReferencia.document(cita.Id_Cita).set(cita).await()
    }

    // Para actualizar el estado de la cita con corrutinas
    suspend fun actualizarEstado(idCita: String, nuevoEstado: String) {
        citasReferencia.document(idCita).update("estado", nuevoEstado).await()
    }

    fun obtenerCitasPorPaciente(idUsuario: String, callback: (List<Cita>) -> Unit) {
        db.collection("Cita")
            .whereEqualTo("id_usuario", idUsuario)
            .get()
            .addOnSuccessListener { result ->
                callback(result.toObjects(Cita::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun obtenerCitasPorMedico(idMedico: String, callback: (List<Cita>) -> Unit) {
        db.collection("Cita")
            .whereEqualTo("id_medico", idMedico)
            .get()
            .addOnSuccessListener { result ->
                callback(result.toObjects(Cita::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }
}