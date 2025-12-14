package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.CentroMedico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.CentroMedicoRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.MedicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CentroMedicoViewModel(
    private val medicoRepo: MedicoRepository = MedicoRepository(),
    private val centroRepo: CentroMedicoRepository = CentroMedicoRepository()
): ViewModel() {

    // Estado que va a contener el objeto Medico actual paa acceder al id del centro medico
    private val _medico = MutableStateFlow<Medico?>(null)
    val medico: StateFlow<Medico?> = _medico

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _actualizacionExitosa= MutableStateFlow(false)
    val actualizacionExitosa: StateFlow<Boolean> = _actualizacionExitosa

    private val _centrosDisponibles = MutableStateFlow<List<CentroMedico>>(emptyList())
    val centrosDisponibles: StateFlow<List<CentroMedico>> = _centrosDisponibles

    // Funcion que carga la informacion del medico con su centro actual
    fun cargarMedico(idMedico: String){
        if(_medico.value != null) return // Si ya tenemos los datos nos evitamos hacer cargas que realenticen el sistema

        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            val med = medicoRepo.obtenerMedicoPorId(idMedico)

            _medico.value = med

            if(med == null){
                _error.value = "No se pudo cargar la informacion del médico"
            }
            _loading.value = false
        }
    }

    // Funcion que nos va a ayudar a cargar los centros medicos para el desplegable
    fun cargarCentrosDisponibles(){
        viewModelScope.launch {
            _centrosDisponibles.value = centroRepo.obtenerTodosLosCentros()
        }
    }

    fun actualizarPerfilMedico(medicoActualizado: Medico) {
        _loading.value = true
        _error.value = null
        _actualizacionExitosa.value = false

        viewModelScope.launch {
            // Llamamos al repositorio para actualizar los campos
            val exito = medicoRepo.actualizarPerfilMedico(medicoActualizado)

            // Si es exitoso el cambio en la base de datos, se actualiza el estado local
            if (exito){
                _medico.value = medicoActualizado
                _actualizacionExitosa.value = true
            } else {
                _error.value = "Erroral guardar el nuevo perfil médico"
            }
            _loading.value = false
        }
    }

    // Hacemos que el snackbar no se muestre de nuevo
    fun noSnackBackRepeat(){
        _actualizacionExitosa.value = false
    }
}