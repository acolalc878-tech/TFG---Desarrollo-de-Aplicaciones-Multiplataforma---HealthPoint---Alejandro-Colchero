package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.CitasRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.SolicitudCitaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MedicoViewModel(
    private val citaRepo: CitasRepository = CitasRepository(),
    private val medicoRepository: MedicoRepository = MedicoRepository(),
    private val solicitudRepo: SolicitudCitaRepository = SolicitudCitaRepository()
) : ViewModel() {

    val citasFinalizadas = MutableStateFlow<List<Cita>>(emptyList())
    val solicitudesPendientes = MutableStateFlow<List<SolicitudCita>>(emptyList())

    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    val medico = MutableStateFlow<Medico?>(null)

    var currentMedicoId: String? = null
        private set

    fun cargarMedicoPorUsuario(idUsuario: String) {
        loading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val med = medicoRepository.obtenerMedicoPorId(idUsuario)

                if (med != null) {
                    medico.value = med
                    currentMedicoId = med.Id_medico

                    // Ahora cargamos AMBAS listas: citas finalizadas y solicitudes
                    cargarCitasFinalizadas(med.Id_medico)
                    cargarSolicitudesPendientes(med.Id_medico)

                } else {
                    medico.value = null
                    error.value = "No se encontró médico asociado a este usuario (ID: $idUsuario)"
                }
            } catch (e: Exception) {
                error.value = "Error en la identificación del médico: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    suspend fun cargarCitasFinalizadas(idMedico: String) {
        loading.value = true
        viewModelScope.launch {
            try{
                val lista = citaRepo.obtenerCitasPorMedico(idMedico)
                citasFinalizadas.value = lista
            }catch (e: Exception){
                error.value = "Error al cargar las citas finalizadas: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    fun cargarSolicitudesPendientes(idMedico: String) {
        if (loading.value || idMedico.isBlank()) return

        loading.value = true
        viewModelScope.launch {
            try{
                val lista = solicitudRepo.obtenerSolicitudesPorMedico(idMedico)
                // 🛑 AÑADIMOS ESTA LÍNEA DE DIAGNÓSTICO FINAL:
                Log.d("DIAG_FINAL_UI", "Lista final de solicitudes para la UI: ${lista.size}")

                solicitudesPendientes.value = lista

                solicitudesPendientes.value = lista
            }catch (e: Exception){
                error.value = "Error al cargar las solicitudes pendientes: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }

    fun aceptarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            try {
                solicitudRepo.aceptarSolicitud(solicitud)
                // Recargar solo si el ID de médico está disponible
                medico.value?.Id_medico?.let { cargarSolicitudesPendientes(it) }
            } catch (e: Exception) {
                error.value = "Error al aceptar la solicitud: ${e.message}"
            }
        }
    }

    fun rechazarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            try {
                solicitudRepo.rechazarSolicitud(solicitud)
                // Recargar solo si el ID de médico está disponible
                medico.value?.Id_medico?.let { cargarSolicitudesPendientes(it) }
            } catch (e: Exception) {
                error.value = "Error al rechazar la solicitud: ${e.message}"
            }
        }
    }
}