package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.SolicitudCitaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SolicitarCitasViewModel(
    private val repo: SolicitudCitaRepository = SolicitudCitaRepository(),
    private val repoMedico: MedicoRepository = MedicoRepository()
) : ViewModel() {

    private val _especialidades = MutableStateFlow<List<String>>(emptyList())
    val especialidades: StateFlow<List<String>> = _especialidades

    private val _medicos = MutableStateFlow<List<Medico>>(emptyList())
    val medicos: StateFlow<List<Medico>> = _medicos

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _solicitudes = MutableStateFlow<List<SolicitudCita>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudCita>> = _solicitudes

    fun cargarSolicitudes(idMedico: String) {
        viewModelScope.launch {
            _solicitudes.value = repo.obtenerSolicitudesPorMedico(idMedico)
        }
    }

    fun aceptarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            try{
                repo.aceptarSolicitud(solicitud)
                cargarSolicitudes(solicitud.Id_medico)
            }catch (e: Exception){
                println("Error al aceptar solicitud: ${e.message}")
            }
        }
    }

    fun rechazarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            try{
                repo.rechazarSolicitud(solicitud)
                cargarSolicitudes(solicitud.Id_medico)
            }catch (e: Exception){
                println("Error al rechazar solicitud: ${e.message}")
            }
        }
    }

    fun cargarEspecialidades(){
        viewModelScope.launch {
            _mensaje.value = null
            _especialidades.value = repoMedico.obtenerTodasLasEspecialidades()
        }
    }

    fun enviarSolicitud(idPaciente: String, medico: Medico, motivo: String) {
        viewModelScope.launch {
            _mensaje.value = null
            try {

                val nuevaSolicitud = SolicitudCita(
                    Id_solicitud = UUID.randomUUID().toString(),
                    Id_usuario = idPaciente,
                    Id_medico = medico.Id_medico,
                    especialidad = medico.especialidad,
                    motivo = motivo,
                    estado = "PENDIENTE",
                    fechaSolicitud = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                )

                repo.enviarSolicitud(nuevaSolicitud)

                _mensaje.value = "Solicitud enviada al medico. ${medico.nombre} con éxito"

                _medicos.value = emptyList()

                _especialidades.value = emptyList()

            } catch (e: Exception){
                _mensaje.value = "Error al enviar la solicitud: ${e.localizedMessage}"
            }
            }
        }

    fun cargarMedicos(especialidad: String) {
        viewModelScope.launch {
            if (especialidad.isNotBlank()){
                _medicos.value = repoMedico.obtenerMedicosPorEspecialidad(especialidad)
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}