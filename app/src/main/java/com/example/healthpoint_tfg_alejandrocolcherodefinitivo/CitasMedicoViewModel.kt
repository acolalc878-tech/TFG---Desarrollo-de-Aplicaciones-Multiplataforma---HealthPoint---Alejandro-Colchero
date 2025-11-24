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

    // Funcion para cargar las citas del médico
    fun cargarCitasMedico(idMedico: String) {
        viewModelScope.launch {
            _loading.value = true
            _citas.value = repo.obtenerCitasPorMedico(idMedico)
            _loading.value = false
        }
    }

    // Creamos una nueva cita
    fun crearCita(cita: Cita){
        viewModelScope.launch {
            _loading.value = true
            try{
                repo.crearCita(cita)
                _mensaje.value = "Cita creada con exito"
            } catch (e: Exception){
                _mensaje.value = "Error al crear la cita: ${e.message}"
            }
            _loading.value
        }
    }

    // Editar cita del medico
    fun editarCitaMedico(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            try{
                repo.editarCira(cita)
                _mensaje.value = "Cita modificada con exito"

            } catch (e: Exception){
                _mensaje.value = "Error al modificar la cita: ${e.message}"
            }
            _loading.value = false
        }
    }

    // Para cambiar de estado a la cita
    fun cambiarEstado(idCita: String, estado: String){
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.actualizarEstado(idCita, estado)
                _mensaje.value = "Estado cambiado con éxito"
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar el estado: ${e.message}"
            }
            _loading.value = false
        }
    }
}