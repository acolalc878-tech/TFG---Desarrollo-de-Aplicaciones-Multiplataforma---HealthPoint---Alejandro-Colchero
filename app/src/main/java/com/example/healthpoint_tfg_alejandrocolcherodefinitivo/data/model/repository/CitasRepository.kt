package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitasRepository {

    // Iniciamos la instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    // Referencia directa a la coleccion Cita para hacer llamadas de forma mas sencilla
    private val coleccion = db.collection("Cita")

    // Función que recupera todas las citas asociadas a un medico especifico
    suspend fun obtenerCitasPorMedico(idMedico: String): List<Cita> {

        // Ejecutamos una consulta con filtro: recuperamos los documentos donde id_medico coincide con el ID proporcionado
        val snap = coleccion
            .whereEqualTo("id_medico", idMedico)
            .get()
            .await() // Suspendemos la ejecucion hasta que los resultados de Firestore esten cargados

        // Mapeamos los documentos de la consulta a una lista de objetos Cita
        return snap.documents.mapNotNull { it.toObject(Cita::class.java) } // mapNotNull asegura que se incluyan solo los objetos que mapean correctamente
    }

    // Metodo que actualiza el campo "estado" de los documentos de la coleccion Cita para marcarlo como "FINALIZADA"
    suspend fun finalizarCita(citaId: String){
        try {
            coleccion.document(citaId)
                .update("estado", "FINALIZADA")
                .await()
        }catch (e: Exception){
            throw e
        }
    }
}