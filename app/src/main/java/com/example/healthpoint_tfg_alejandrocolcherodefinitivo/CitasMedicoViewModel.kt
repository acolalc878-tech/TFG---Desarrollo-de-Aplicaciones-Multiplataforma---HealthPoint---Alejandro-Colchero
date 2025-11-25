package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CitasMedicoViewModel : ViewModel() {

    private val repo = CitaRepository()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> get() = _citas

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> get() = _mensaje

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    fun cargarCitasMedico(idMedico: String) {
            _loading.value = true
            repo.obtenerCitasPorMedico(idMedico){ listaCitas ->
                _citas.value = listaCitas
                _loading.value = false
        }
    }

    fun crearCita(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            val ok = repo.crearCita(cita)
            _mensaje.value = if (ok) "Cita creada con éxito" else "Error al crear cita"
            _loading.value = false
        }
    }

    fun editarCita(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            val ok = repo.editarCira(cita)
            _mensaje.value = if (ok) "Cita editada" else "Error editando cita"
            _loading.value = false
        }
    }

    fun cambiarEstado(idCita: String, nuevoEstado: String) {
        viewModelScope.launch {
            _loading.value = true
            val ok = repo.actualizarEstado(idCita, nuevoEstado)
            _mensaje.value = if (ok) "Estado actualizado" else "Error cambiando estado"
            _loading.value = false
        }
    }
}