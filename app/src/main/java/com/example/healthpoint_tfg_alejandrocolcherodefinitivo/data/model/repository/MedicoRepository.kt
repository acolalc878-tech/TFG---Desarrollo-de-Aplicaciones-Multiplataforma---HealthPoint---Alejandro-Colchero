package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository

import android.util.Log
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MedicoRepository {

    // Instancia a Firestore
    private val db = FirebaseFirestore.getInstance()
    // Referencia a la coleccion Medico para hacer llamadas de forma mas simple
    private val medicos = db.collection("Medico")

    // Funcion que recupera un documento Medico usando su ID del documento como clave de acceso
    suspend fun obtenerMedicoPorId(idMedico: String): Medico? {
        if(idMedico.isBlank()) return null // Retornamos null si esta vacío el ID

        return try {
            // Acceso directo al id, esperando resultados
            val snapshot = medicos.document(idMedico).get().await()

            // Nos aseguramos de que el Id del medico se guarde en minusculas en el objeto
            val idFiltroMedico = (snapshot.getString("Id_medico") ?: snapshot.id).lowercase()

            // Mapeamos del documento a objeto, y sobreescribimos el id con el valor cambiado a minusculas
            return snapshot.toObject(Medico::class.java)?.copy(
                Id_medico = idFiltroMedico
            )

        } catch (e: Exception) {
            // Manejo de error
            Log.e("MedicoRepo", "Error al obtener médico por ID: $idMedico", e)
            return null
        }
    }

    // Funcion que busca y recupera una lista de medicos con una especialidad concreta
    suspend fun obtenerMedicosPorEspecialidad(especialidad: String): List<Medico> {

        if(especialidad.isBlank()) return emptyList() // Si no hay especialidad devuelve una lista vacia

        return try{
            // Consulta con filtro: por el campo de especialidad
            val snapshot = db.collection("Medico")
                .whereEqualTo("especialidad", especialidad)
                .get()
                .await()

            // Mapeo y convertimos de nuevo el ID a minusculas
            return snapshot.documents.mapNotNull { doc ->
                val medico = doc.toObject(Medico::class.java)

                // Obtenemos el id y lo transformamos antes de entregarlo
                val idMedic = doc.getString("Id_medico") ?: doc.id

                medico?.copy(Id_medico = idMedic.lowercase())
            }
        } catch (e: Exception) {
            Log.e("MedicoRepo", "Error al obtener médicos por especialidad: $especialidad", e)
            return emptyList()
        }
    }

    // Funcion que devuelve una lista con todas las especialidades en la coleccion Medico
    suspend fun obtenerTodasLasEspecialidades(): List<String> {
        return try {
            // Recupera toda la colección (advertencia: puede ser ineficiente en colecciones grandes).
            val snapshot = db.collection("Medico")
                .get()
                .await()

            // Extraemos el valor del campo especialidad de cada documento y extraemos solo los valores unicos
            return snapshot.documents
                .mapNotNull { it.getString("especialidad")}
                .distinct()
        } catch (e : Exception) {
            return emptyList()
        }
    }

    // Funcion IMPORTANTE USADA DURANTE EL INICIO DE SESION DEL MEDICO - Buscamos el perfil del medico
    // usando el ID de Firebase Authentication
    suspend fun obtenerMedicoPorUsuarioId(idUsuario: String): Medico? {
        if (idUsuario.isBlank()) return null // si el id esta vacio devolvemos nulo

        try {
            // Consulta con filtro por el ID de Firebase Authentication, limitandolo a un resultado
            val snapshot = medicos
                .whereEqualTo("Id_usuario", idUsuario)
                .limit(1) // nada mas coincidir con un, paramos la busqueda
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                val doc = snapshot.documents.first()

                // Normalizamos el ID de nuevo, pasamos a minusculas el ID entero
                val idFiltroMedico = (doc.getString("Id_medico") ?: doc.id).lowercase()

                // Mapeamos y entregamosel modelo con el ID formateado a minusculas
                return doc.toObject(Medico::class.java)?.copy(
                    Id_medico = idFiltroMedico
                )

            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    // Funcion para actualizar los campos especificos del perfil de un medico
    suspend fun actualizarPerfilMedico(medico: Medico): Boolean {
        if (medico.Id_medico.isBlank()) return false // si tiene un id vacio retornamos falso

        try {
            // Mapa de datos con los campos que se van a modificar.
            val actualizaciones = hashMapOf<String, Any>(
                "especialidad" to medico.especialidad.trim(),
                "horario" to medico.horario.trim(),
                "numColegiado" to medico.numColegiado.trim()
            )

            // Referencia al documento: se normaliza el ID de nuevo para asegurarnos que coincidan los documentos exactamente
            medicos.document(medico.Id_medico.lowercase())
                .update(actualizaciones)
                .await()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}