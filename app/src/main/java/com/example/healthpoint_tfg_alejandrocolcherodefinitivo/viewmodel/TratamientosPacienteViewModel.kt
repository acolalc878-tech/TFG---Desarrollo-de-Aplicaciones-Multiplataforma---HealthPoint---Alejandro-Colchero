package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TratamientosPacienteViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _tratamientos = MutableStateFlow<List<Tratamiento>>(emptyList())
    val tratamientos: StateFlow<List<Tratamiento>> = _tratamientos

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error= mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun cargarTratamientosPorPaciente(idPaciente: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val citasSnapshot = db.collection("Cita")
                    .whereEqualTo("id_usuario", idPaciente)
                    .get()
                    .await()

                val idCitasPaciente = citasSnapshot.documents.map { it.id }

                if(idCitasPaciente.isEmpty()){
                    _tratamientos.value = emptyList()
                    return@launch
                }

                val tratamientosSnapshot = db.collection("Tratamiento")
                    .whereIn("id_cita", idCitasPaciente.take(10))
                    .get()
                    .await()

                _tratamientos.value = tratamientosSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Tratamiento::class.java)?.copy(Id_tratamiento = doc.id)
                }

            } catch (e: Exception) {
                _error.value = "Error al cargar los tratamientos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}