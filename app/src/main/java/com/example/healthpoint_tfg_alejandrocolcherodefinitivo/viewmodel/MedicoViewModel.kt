package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.CitasRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MedicoViewModel(
    private val citaRepo: CitasRepository = CitasRepository(),
    private val medicoRepository: MedicoRepository = MedicoRepository()
) : ViewModel() {

    val citasMedico = MutableStateFlow<List<Cita>>(emptyList())

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
                // Cargar citas automaticamente para el medico
                cargarCitasAsignadasMedico(med.Id_medico)
            } else {
                medico.value = null
                error.value = "No se encontró médico asociado a este usuario"
            }
            loading.value = false
        }
    }

    fun cargarCitasAsignadasMedico(idMedico: String) {
        loading.value = true

        viewModelScope.launch {
            try{
                val lista = citaRepo.obtenerCitasPorMedico(idMedico)
                citasMedico.value = lista
            }catch (e: Exception){
                error.value = "Error al cargar las citas: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }
}