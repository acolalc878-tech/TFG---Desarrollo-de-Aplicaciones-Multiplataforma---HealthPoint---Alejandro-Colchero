package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SolicitudCitaRepository {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()
    // Instancia de la coleccion SolicitudCita para llamadas sencillas
    private val solicitudCita = db.collection("SolicitudCita")

    // Funcion que va a recuperar las solicitudes PENDIENTES para cada medico concretado en esa solicitud
    suspend fun obtenerSolicitudesPorMedico(idMedicoNormalizado: String): List<SolicitudCita> {
        try{
            // Consulta con filtros: hacemos un filtro por el ID del medico (ya normalizado) y el estado como PENDIENTE
            val snapshot = db.collection("SolicitudCita")
                .whereEqualTo("id_medico", idMedicoNormalizado)
                .whereEqualTo("estado", "PENDIENTE")
                .get().await() // Esperamos respuesta de la base de datos

            // Mapeo y asignacion del ID
            return snapshot.documents.mapNotNull { doc ->
                val solicitud = doc.toObject(SolicitudCita::class.java)

                // Asignamos el ID del documento de Firestore al campo del modelo de datos en Kotlin
                solicitud?.copy(id_solicitud = doc.id)
            }

        } catch (e: Exception){
            return emptyList() // Lista vacia en caso de error
        }
    }

    // Funcion que modifica el estado de la solicitud a "ACEPTADA"
    suspend fun aceptarSolicitud(solicitud: SolicitudCita) {
        try{
            // Accedemos al documento usando el ID de la solicitud
            solicitudCita.document(solicitud.id_solicitud)
                // Actualizamos el campo del estado
                .update("estado", "ACEPTADA")
                .await()
        } catch (e: Exception){
            throw e
        }
    }

    // Funcion que modifica el estado de la solicitud a "RECHAZADA"
    suspend fun rechazarSolicitud(solicitud: SolicitudCita) {
        try{
            // Accedemos al documento usando el ID de la solicitud
            solicitudCita.document(solicitud.id_solicitud)
                // Actualizamos el campo del estado
                .update("estado", "RECHAZADA")
                .await()
        } catch (e: Exception){
            throw e
        }
    }

    // Funcion que crea o sobrescribe un documento de solicitud en la coleccion SolicitudCita
    suspend fun enviarSolicitud(solicitud: SolicitudCita) {
        try{
            solicitudCita.document(solicitud.id_solicitud)
                .set(solicitud)
                .await()
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun obtenerSolicitudesPorPaciente(idPaciente: String): List<SolicitudCita> {
        return try {
            val snapshot = db.collection("SolicitudCita")
                .whereEqualTo("id_usuario", idPaciente) // Filtra por el ID del usuario/paciente
                .get().await()

            snapshot.documents.mapNotNull { doc ->
                val solicitud = doc.toObject(SolicitudCita::class.java)
                solicitud?.copy(id_solicitud = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}