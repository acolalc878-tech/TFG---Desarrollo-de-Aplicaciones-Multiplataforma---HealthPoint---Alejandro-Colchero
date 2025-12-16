package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.SolicitudCitaRepository
import com.google.firebase.firestore.FirebaseFirestore
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

    private val db = FirebaseFirestore.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _especialidades = MutableStateFlow<List<String>>(emptyList())
    val especialidades: StateFlow<List<String>> = _especialidades

    private val _medicos = MutableStateFlow<List<Medico>>(emptyList())
    val medicos: StateFlow<List<Medico>> = _medicos

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _solicitudes = MutableStateFlow<List<SolicitudCita>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudCita>> = _solicitudes

    fun cargarSolicitudes(idMedico: String) {
       if(idMedico.isBlank()) return

        Log.i("DebugCarga", "Cargando solicitudes con ID de filtro: $idMedico")

       viewModelScope.launch {
           Log.i("CitasViewModel", "Iniciando carga de solicitudes para ID: $idMedico")

           _loading.value = true
           try {
               val listaSolicitudes = repo.obtenerSolicitudesPorMedico(idMedico)

               Log.i("CitasViewModel", "Solicitudes encontradas para $idMedico: ${listaSolicitudes.size}")

               _solicitudes.value = listaSolicitudes
           } catch (e: Exception) {
               println("Error al cargar solicitudes para el médico: ${e.message}")
               _solicitudes.value = emptyList()
           } finally {
               _loading.value = false
           }
       }
    }

    fun aceptarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            repo.aceptarSolicitud(solicitud)
            cargarSolicitudes(solicitud.id_medico)
        }
    }

    fun rechazarSolicitud(solicitud: SolicitudCita) {
        viewModelScope.launch {
            repo.rechazarSolicitud(solicitud)
            cargarSolicitudes(solicitud.id_medico)
        }
    }

    fun cargarEspecialidades(){
        viewModelScope.launch {
            _mensaje.value = null
            _especialidades.value = repoMedico.

            obtenerTodasLasEspecialidades()
        }
    }

    fun enviarSolicitud(idPaciente: String, medico: Medico, motivo: String) {
        viewModelScope.launch {
            _mensaje.value = null
            try {
                val idSolicitudGenerada = UUID.randomUUID().toString()
                val nuevaSolicitud = SolicitudCita(
                    id_solicitud = idSolicitudGenerada,
                    id_usuario = idPaciente.lowercase(),
                    id_medico = medico.Id_medico.lowercase(),
                    especialidad = medico.especialidad,
                    motivo = motivo,
                    estado = "PENDIENTE",
                    fechaSolicitud = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                )

                repo.enviarSolicitud(nuevaSolicitud)

                _mensaje.value = "Solicitud enviada al medico. ${medico.nombre} con éxito|${idSolicitudGenerada}"

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