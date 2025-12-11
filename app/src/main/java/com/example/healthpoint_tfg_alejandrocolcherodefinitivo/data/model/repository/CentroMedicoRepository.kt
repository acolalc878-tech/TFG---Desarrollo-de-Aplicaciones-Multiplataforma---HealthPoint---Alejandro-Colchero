package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import android.util.Log
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.CentroMedico
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CentroMedicoRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun obtenerTodosLosCentros(): List<CentroMedico>{
        return try {
            val snapshot = db.collection("CentroMedico").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(CentroMedico::class.java)?.copy(Id_centroMedico = doc.id)
            }
        } catch (e: Exception) {
            Log.e("CentroRepo", "Error al obtener los centros disponibles" ,e)
            emptyList()
        }
    }
}