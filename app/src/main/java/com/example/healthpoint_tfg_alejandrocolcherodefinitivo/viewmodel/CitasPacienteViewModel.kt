package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.CitaDisplay
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CitasPacienteViewModel(
    private val medicoRepo: MedicoRepository = MedicoRepository()
): ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<CitaDisplay>>(emptyList())
    val citas: StateFlow<List<CitaDisplay>> = _citas

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
                    viewModelScope.launch {
                        val citasNormales = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Cita::class.java)?.copy(id_cita = doc.id)
                        }

                        val citasConNombre = citasNormales.map { cita ->
                            val medico = medicoRepo.obtenerMedicoPorId(cita.id_medico)

                            val nombreCompleto = "${medico?.nombre ?: "Médico"} ${medico?.apellidos ?: "Desconocido"}"

                            CitaDisplay(
                                cita = cita,
                                nombreMedico = nombreCompleto
                            )
                        }

                        _citas.value = citasConNombre
                    }
                }
                _loading.value = false
            }
    }
}
