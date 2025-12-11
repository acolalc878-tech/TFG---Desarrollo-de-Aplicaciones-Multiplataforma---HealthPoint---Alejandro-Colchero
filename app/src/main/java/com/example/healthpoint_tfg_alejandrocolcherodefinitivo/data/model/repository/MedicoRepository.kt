package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import android.util.Log
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MedicoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val medicoCollection= db.collection("Medico")

    fun obtenerMedicoPorUsuario(idUsuario: String, callback:(Medico?) -> Unit) {
        medicoCollection
            .whereEqualTo("Id_usuario", idUsuario)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                val medico= doc?.toObject(Medico::class.java)
                callback(medico)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // Funcion que nos ayuda a obtener la informacion de un medico mediante la id de su usuario
    suspend fun obtenerMedicoPorId(idMedico: String): Medico?{
        return try{
            val snapshot = medicoCollection.document(idMedico).get().await()

            if(snapshot.exists()) {
                snapshot.toObject(Medico::class.java)?.copy(Id_medico = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception){
            Log.e("MedicoRepo", "Error al obtener el medico por su ID: $idMedico", e)
            null
        }
    }

    // Funcion que actualiza el Id_centroMedico para un medico especifico
    suspend fun actualizarCentroMedico(idMedico: String, nuevoIdCentro: String): Boolean {
        return try {
            // Usamos el id del centro medico del documento para actualizar
            medicoCollection.document(idMedico)
                .update("Id_centroMedico", nuevoIdCentro)
                .await()
            true
        } catch (e: Exception){
            Log.e("MedicoRepo", "Error al actualizar centro medico: $idMedico", e)
            false
        }
    }

    // Guardamos los campos editables del perfil en Firebase
    suspend fun actualizarPerfilMedico(medicoActualizado: Medico): Boolean {
        return try{
            val updates = hashMapOf<String, Any>(
                "Id_centroMedico" to medicoActualizado.Id_CentroMedico,
                "nombre" to medicoActualizado.nombre,
                "apellidos" to medicoActualizado.apellidos,
                "especialidad" to medicoActualizado.especialidad,
                "horario" to medicoActualizado.horario,
                 "numColegiado" to medicoActualizado.numColegiado
            )

            medicoCollection.document(medicoActualizado.Id_medico)
                .update(updates)
                .await()
            true
        } catch (e : Exception) {
            Log.e("MedicoRepo", "Error al actualizar perfil médico: ${medicoActualizado.Id_medico}", e)
            false
        }
    }
}