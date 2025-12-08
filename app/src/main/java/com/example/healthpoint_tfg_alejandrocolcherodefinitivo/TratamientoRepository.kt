package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TratamientoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val tratamientoReferencoia = db.collection("Tratamiento")
    private val citasReferencia = db.collection("Cita")

    // Funcion para crear un tratamiento con corrutinas y que nos retorna true si el tratamiento se ha creado
    suspend fun crearTratamiento(tratamiento: Tratamiento): Boolean {
        val documentoReferencia = tratamientoReferencoia.document()
        val nuevo = tratamiento.copy(Id_tratamiento = documentoReferencia.id)
        documentoReferencia.set(nuevo).await()
        return true
    }

    suspend fun editarTratamiento(tratamiento: Tratamiento) {
        tratamientoReferencoia.document(tratamiento.Id_tratamiento).set(tratamiento).await()
    }

    suspend fun eliminarTratamiento(idTratamiento: String){
        tratamientoReferencoia.document(idTratamiento).delete().await()
    }

    suspend fun obtenerTratamientosPorCita(idCita: String): List<Tratamiento> {
        val snap= tratamientoReferencoia.whereEqualTo("Id_cita", idCita).get().await()
        return snap.documents.mapNotNull { document ->
            try{
                Tratamiento(
                    Id_tratamiento = document.id,
                    Id_cita = document.getString("Id_cita")?: "",
                    descripcion = document.getString("descripcion")?: "",
                    duracionDias = document.getString("duracionDias")?.toInt()?: 0,
                    indicaciones = document.getString("indicaciones")?: ""
                )
            } catch (e: Exception){
                null
            }
        }
    }

    suspend fun obtenerTratamientosPorMedico(idMedico: String): List<Tratamiento> {

        val citasSnap = citasReferencia.whereEqualTo("Id_medico", idMedico).get().await()
        val idCitas = citasSnap.documents.map { it.id }
        if (idCitas.isEmpty()) return emptyList()
        val resultados = mutableListOf<Tratamiento>()
        val chunkSize = 10
        var i = 0

        while (i < idCitas.size) {
            val chunk = idCitas.subList(i, minOf(i + chunkSize, idCitas.size))
            val snap = tratamientoReferencoia.whereIn("Id_cita", chunk).get().await()
            val lista = snap.documents.mapNotNull { document ->
                try{
                    Tratamiento(
                        Id_tratamiento = document.id,
                        Id_cita = document.getString("Id_cita") ?: "",
                        descripcion = document.getString("descripcion")?: "",
                        duracionDias = document.getLong("duracion")?.toInt() ?: 0,
                        indicaciones = document.getString("indicaciones")?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
            resultados.addAll(lista)
            i += chunkSize
        }
        return resultados
    }
}