package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MedicoViewModel(
    private val citaRepo: CitaRepository = CitaRepository(),
    private val usuarioRepo: UsuarioRepository = UsuarioRepository(),
    private val medicoRepository: MedicoRepository = MedicoRepository()
) : ViewModel() {

    val citasMedico = MutableStateFlow<List<Cita>>(emptyList())
    val pacientesEncontrados = MutableStateFlow<List<Usuario>>(emptyList())
    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    // Estado del medico actual (nulo hasta que se cargue)
    val medico = MutableStateFlow<Medico?>(null)

    // Id_medico resuelto (nulo si no se ha resuelto aun)
    var currentMedicoId: String? = null
        private set


    // Cargamos citas asignadas del medico
    fun cargarMedicoPorUsuario(idUsuario: String) {
        loading.value = true
        medicoRepository.obtenerMedicoPorUsuario(idUsuario) { med ->
            if (med != null) {
                medico.value = med
                currentMedicoId = med.Id_medico
                // Cargar citas automáticamente para el medico
                cargarCitasAsignadasMedico(med.Id_medico)
            } else {
                // Si no se encontró ningun medico dejamos medico como nulo y un mensaje de error
                medico.value = null
                error.value = "No se encontró médico asociado a este usuario"
            }
            loading.value = false
        }
    }

    // Cargar citas del médico (usa callback-based repo)
    fun cargarCitasAsignadasMedico(idMedico: String) {
        loading.value = true
        citaRepo.obtenerCitasPorMedico(idMedico) { lista ->
            citasMedico.value = lista
            loading.value = false
        }
    }

    // Crear cita
    fun crearCita(cita: Cita) {
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.crearCita(cita)
                // refrescar
                cargarCitasAsignadasMedico(cita.Id_medico)
            } catch (e: Exception) {
                error.value = "Error al crear cita: ${e.message}"
                loading.value = false
            }
        }
    }

    // Editar cita (suspend fn en repo)
    fun editarCita(cita: Cita) {
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.editarCira(cita)
                cargarCitasAsignadasMedico(cita.Id_medico)
            } catch (e: Exception) {
                error.value = "Error al editar cita: ${e.message}"
                loading.value = false
            }
        }
    }

    // Cambiar estado
    fun cambiarEstadoCita(idCita: String, nuevoEstado: String, idMedico: String) {
        viewModelScope.launch {
            try {
                loading.value = true
                citaRepo.actualizarEstado(idCita, nuevoEstado)
                cargarCitasAsignadasMedico(idMedico)
            } catch (e: Exception) {
                error.value = "Error al actualizar estado: ${e.message}"
                loading.value = false
            }
        }
    }

    fun buscarPacientesNombre(nombre: String) {
        usuarioRepo.buscarPacientesPorNombre(nombre) { lista ->
            pacientesEncontrados.value = lista
        }
    }
}