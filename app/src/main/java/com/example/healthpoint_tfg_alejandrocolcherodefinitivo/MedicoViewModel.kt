package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MedicoViewModel(
    private val citaRepo: CitaRepository = CitaRepository(),
    private val usuarioRepo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    val citasMedico = MutableStateFlow<List<Cita>>(emptyList())
    val pacientesEncontrados = MutableStateFlow<List<Usuario>>(emptyList())
    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)


    // Cargamos citas asignadas del medico
    fun cargarCitasAsignadasMedico(idMedico: String) {
        loading.value = true

        citaRepo.obtenerCitasPorMedico(idMedico) { lista ->
            citasMedico.value = lista
            loading.value = false
        }
    }

    // Crear cita con un paciente
    fun crearCita(cita: Cita) {
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.crearCita(cita)
                cargarCitasAsignadasMedico(cita.Id_medico)
            } catch (e: Exception) {
                error.value = "Error al crear la cita: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    // Editar citas
    fun editarCita(cita: Cita){
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.editarCira(cita)
                cargarCitasAsignadasMedico(cita.Id_medico)
            } catch (e: Exception) {
                error.value = "Error al crear la cita: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    // Cambiar estado de una cita a (Pendiente o Realizada)
    fun cambiarEstadoCita(idCita: String, nuevoEstado: String, idMedico: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.actualizarEstado(idCita, nuevoEstado)
                cargarCitasAsignadasMedico(idMedico)
            } catch (e: Exception) {
                error.value = "Error al actualizar estado: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    // Buscar pacientes
    fun buscarPacientesNombre(nombre: String) {
        viewModelScope.launch {
            try {
                usuarioRepo.buscarPacientesPorNombre(nombre) { lista ->
                    pacientesEncontrados.value = lista
                }
            } catch (e: Exception) {
                error.value = "Error al buscar pacientes: ${e.message}"
            }
        }
    }

}