package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val citasReferencia = db.collection("Cita")

    // Creamos Cita con corrutinas
    suspend fun crearCita(cita: Cita): Boolean {
        return try{
            val docRef = citasReferencia.document()
            val citaConId = cita.copy(Id_Cita = docRef.id)
            docRef.set(citaConId).await()
            true
        } catch (e:Exception){
            false
        }
    }

    // Para editar la cita con corrutina
    suspend fun editarCira(cita: Cita): Boolean {
        return try{
            citasReferencia.document(cita.Id_Cita).set(cita).await()
            true
        } catch (e:Exception){
            false
        }
    }

    // Para actualizar el estado de la cita con corrutinas
    suspend fun actualizarEstado(idCita: String, nuevoEstado: String): Boolean {
        return try {
            citasReferencia.document(idCita).update("estado", nuevoEstado).await()
            true
        } catch (e:Exception){
            false
        }
    }

    fun obtenerCitasPorPaciente(idUsuario: String, callback: (List<Cita>) -> Unit) {
        db.collection("Cita")
            .whereEqualTo("Id_usuario", idUsuario)
            .get()
            .addOnSuccessListener { result ->
                callback(result.toObjects(Cita::class.java))
            }
            .addOnFailureListener { callback(emptyList()) }
    }

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