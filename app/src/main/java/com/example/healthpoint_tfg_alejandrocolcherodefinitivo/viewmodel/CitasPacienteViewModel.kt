package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CitasPacienteViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarCitasPorUsuario(idUsuario: String) {
        _loading.value = true
        _error.value = null

        db.collection("Cita")
            .whereEqualTo("id_usuario", idUsuario)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    _error.value = "Error cargando citas"
                    _loading.value = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Cita::class.java)?.copy(id_cita = doc.id)
                    }
                    _citas.value = lista
                }
                _loading.value = false
            }
    }
}
