package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SolicitudCitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val solicitudCita = db.collection("SolicitudCita")

    suspend fun obtenerSolicitudesPorMedico(idMedico: String): List<SolicitudCita> {
       try{
           val snapshot = solicitudCita
               .whereEqualTo("id_medico", idMedico)
               .whereEqualTo("estado", "PENDIENTE")
               .get().await()

          return snapshot.documents.mapNotNull { doc ->
              doc.toObject(SolicitudCita::class.java)?.copy(Id_solicitud = doc.id)
          }

       } catch (e: Exception){
           println("Error al obtener solicitudes para el médico $idMedico: ${e.message}")
           return emptyList()
       }
    }

    suspend fun aceptarSolicitud(solicitud: SolicitudCita) {
        try{
            solicitudCita.document(solicitud.Id_solicitud)
                .update("estado", "ACEPTADA")
                .await()
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun rechazarSolicitud(solicitud: SolicitudCita) {
        try{
            solicitudCita.document(solicitud.Id_solicitud)
                .update("estado", "RECHAZADA")
                .await()
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun enviarSolicitud(solicitud: SolicitudCita) {
        try{
            solicitudCita.document(solicitud.Id_solicitud)
                .set(solicitud)
                .await()
        } catch (e: Exception){
            throw e
        }
    }
}