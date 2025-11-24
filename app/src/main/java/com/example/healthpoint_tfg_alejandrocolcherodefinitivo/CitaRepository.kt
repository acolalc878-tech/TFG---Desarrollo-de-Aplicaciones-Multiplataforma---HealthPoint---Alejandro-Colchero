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

    // Como obtenemos las citas de un paciente con corrutinas
    suspend fun obtenerCitasPorMedico(idMedico: String): List<Cita> {
        val snapshot = citasReferencia
            .whereEqualTo("Id_medico", idMedico)
            .get()
            .await()

        return snapshot.toObjects(Cita::class.java)
    }

    // Listener en tiempo real para las citas de los pacientes
    fun listenCitasUsuario(idUsuario: String, onChange:(List<Cita>) -> Unit
    ): ListenerRegistration {
        return citasReferencia
            .whereEqualTo("Id_usuario", idUsuario).addSnapshotListener{value, e ->
                val lista = value?.toObjects(Cita::class.java)?: emptyList()
                onChange(lista)
            }
    }
}