package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MedicoViewModel(
    private val citaRepo: CitaRepository = CitaRepository(),
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
}