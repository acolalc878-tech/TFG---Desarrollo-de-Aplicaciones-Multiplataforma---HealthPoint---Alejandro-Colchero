package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.CitasRepository
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GestionDatosComunesViewModel(
    private val citaRepo: CitasRepository = CitasRepository(),
    private val usuarioRepo: UsuarioRepository = UsuarioRepository()
) : ViewModel(){

    // Estado para la listade Citas del Médico
    private val _citasMedico = MutableStateFlow<List<Cita>>(emptyList())
    val citasMedico: StateFlow<List<Cita>> = _citasMedico

    private val _pacientes = MutableStateFlow<List<Usuario>>(emptyList())
    val pacientes: StateFlow<List<Usuario>> = _pacientes

    // Cargamos las citas asignadas al medico para ser usadas en desplegables
    fun cargarCitasMedico(idMedico: String){
        viewModelScope.launch {
            try{
                val listaCitas = citaRepo.obtenerCitasPorMedico(idMedico)
                _citasMedico.value = listaCitas
            } catch (e: Exception){
                _citasMedico.value = emptyList()
            }
        }
    }

    fun cargarTodosLosPacientes(){
        viewModelScope.launch {
            try{
                val listaUsuarios = usuarioRepo.obtenerTodosLosUsuarios()
                _pacientes.value = listaUsuarios
            } catch (e: Exception) {
                _pacientes.value = emptyList()
            }
        }
    }
}