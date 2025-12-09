package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TratamientoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val tratamientoReferencoia = db.collection("Tratamiento")

    // Funcion para crear un tratamiento con corrutinas y que nos retorna true si el tratamiento se ha creado
    suspend fun crearTratamiento(tratamiento: Tratamiento): String {
        val documentoReferencia = tratamientoReferencoia.document()
        val nuevo = tratamiento.copy(Id_tratamiento = documentoReferencia.id)
        documentoReferencia.set(nuevo).await()
        return documentoReferencia.id
    }

    suspend fun editarTratamiento(tratamiento: Tratamiento) {
        tratamientoReferencoia.document(tratamiento.Id_tratamiento).set(tratamiento).await()
    }

    suspend fun eliminarTratamiento(idTratamiento: String){
        tratamientoReferencoia.document(idTratamiento).delete().await()
    }

    suspend fun obtenerTratamientosPorMedico(idMedico: String): List<Tratamiento> {
        val snap = tratamientoReferencoia
            .whereEqualTo("Id_medico", idMedico)
            .get()
            .await()
        return snap.documents.mapNotNull { document ->
            document.toObject(Tratamiento::class.java)?.copy(Id_tratamiento = document.id)
        }
    }

    suspend fun obtenerPacientes(): List<Usuario> {
        val snap = FirebaseFirestore.getInstance()
            .collection("Usuario")
            .get()
            .await()

        return snap.documents.mapNotNull { it.toObject(Usuario::class.java) }
    }

    suspend fun obtenerCitasPorPaciente(idPaciente: String): List<Cita> {
        val snap = FirebaseFirestore.getInstance()
            .collection("Cita")
            .whereEqualTo("Id_usuario", idPaciente)
            .get()
            .await()

        return snap.documents.mapNotNull { it.toObject(Cita::class.java) }
    }

    suspend fun obtenerTratamientosPorUsuario(idUsuario: String): List<Tratamiento> {
        val snap = tratamientoReferencoia
            .whereEqualTo("Id_usuario", idUsuario)
            .get()
            .await()
        return snap.documents.mapNotNull { document ->
            document.toObject(Tratamiento::class.java)?.copy(Id_tratamiento = document.id)
        }
    }
}