package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GestionarPacientesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var pacientes by mutableStateOf<List<Paciente>>(emptyList())
        private set

    var busqueda by mutableStateOf("")
        private set

    fun actualizarBusqueda(text: String){
        busqueda = text
        buscarPacientes(text)
    }

    fun buscarPacientes(query: String) {
        if (query.isBlank()) {
            pacientes = emptyList()
            return
        }

        db.collection("Usuario")
            .whereEqualTo("rol", "Paciente")
            .get()
            .addOnSuccessListener { result ->
                pacientes = result.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: ""
                    if (nombre.contains(query, ignoreCase = true)) {
                        Paciente(
                            Id_paciente = doc.id,
                            nombre = nombre,
                            apellidos = doc.getString("apellidos") ?: "",
                            email = doc.getString("email") ?: "",
                            telefono = doc.getString("telefono") ?: "",
                            dni = doc.getString("dni") ?: "",
                            fechaNacimiento = doc.getString("fechaNacimiento") ?: ""
                        )
                    } else null
                }
            }
    }
}