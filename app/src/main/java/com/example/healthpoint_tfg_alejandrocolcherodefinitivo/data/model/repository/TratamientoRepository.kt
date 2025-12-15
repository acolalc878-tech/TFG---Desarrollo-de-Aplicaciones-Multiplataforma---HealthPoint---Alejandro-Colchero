package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TratamientoRepository {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // Instancia de la coleccion Tratamiento para llamadas sencillas
    private val tratamientoReferencoia = db.collection("Tratamiento")


    // Funcion para crear un tratamiento con corrutinas
    suspend fun crearTratamiento(tratamiento: Tratamiento): String {
        // Generamos la refererencia del documento antes de guardar datos, para obtener e ID unico generado de forma anticipada
        val documentoReferencia = tratamientoReferencoia.document()

        // Asignamos dicho ID nuevo generado al modelo de datos de kotlin
        val nuevo = tratamiento.copy(Id_tratamiento = documentoReferencia.id)

        // Escribimos en el documento de Firestore y esperamos la confirmacion
        documentoReferencia.set(nuevo).await()

        // Devolvemos el ID generado
        return documentoReferencia.id
    }

    // Funcion que actualiza un tratamiento que ya existe reemplazando el que ya esta en la coleccion
    suspend fun editarTratamiento(tratamiento: Tratamiento) {
        // Sobrescribe completamente el documento existente.
        tratamientoReferencoia.document(tratamiento.Id_tratamiento).set(tratamiento).await()
    }

    // Funcion que elimina un documento de Tratamiento usando su ID
    suspend fun eliminarTratamiento(idTratamiento: String){
        // Accedemos al documento mediante id y borramos
        tratamientoReferencoia.document(idTratamiento).delete().await()
    }

    // Funcion que obtiene todos los tratamientos que han sido creados o asignados por un medico
    suspend fun obtenerTratamientosPorMedico(idMedico: String): List<Tratamiento> {

        // Consulta con filtro donde buscamos documentos donde el ID del medico coincide con el ID proporcionado.
        val snap = tratamientoReferencoia
            .whereEqualTo("Id_medico", idMedico)
            .get()
            .await()

        // Convertimos los documentos a objetos Tratamiento
        return snap.documents.mapNotNull { document ->
            document.toObject(Tratamiento::class.java)?.copy(Id_tratamiento = document.id)
        }
    }
}