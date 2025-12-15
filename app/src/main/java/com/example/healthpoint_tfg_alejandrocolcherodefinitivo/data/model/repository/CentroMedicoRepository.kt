package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import android.util.Log
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.CentroMedico
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CentroMedicoRepository {

    // Inicializamos la instancia dde Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    // Funcion que obtiene todos los documentos de la coleccion CentroMedico, mapeandolo al mi modelo de dato
    // (CentroMedico)
    suspend fun obtenerTodosLosCentros(): List<CentroMedico>{
        return try {
            // Ejecutamos la consulta a la coleccion y suspendemos la corrutina hasta que reciba un resultado
            val snapshot = db.collection("CentroMedico").get().await()
            // Mapeamos el documento a un Objeto Kotlin
            snapshot.documents.mapNotNull { doc ->
                // Convertimos el documento en Objeto CentroMedico
                doc.toObject(CentroMedico::class.java)?.copy(Id_centroMedico = doc.id)
            }
        } catch (e: Exception) {
            Log.e("CentroRepo", "Error al obtener los centros disponibles" ,e)
            emptyList()
        }
    }
}