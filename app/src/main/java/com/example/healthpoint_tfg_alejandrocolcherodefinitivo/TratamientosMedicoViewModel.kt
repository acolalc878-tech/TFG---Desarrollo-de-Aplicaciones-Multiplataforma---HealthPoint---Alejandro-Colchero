package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TratamientosMedicoViewModel(
    private val repo: TratamientoRepository = TratamientoRepository()
) : ViewModel(){

    private val _tratamientos = MutableStateFlow<List<Tratamiento>>(emptyList())
    val tratamientos: StateFlow<List<Tratamiento>> get() = _tratamientos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    fun cargarTratamientosPorMedico(idMedico: String) {
        viewModelScope.launch {
            _loading.value = true

            try{
                val lista = repo.obtenerTratamientosPorMedico(idMedico)
                _tratamientos.value = lista
                _mensaje.value = null
            } catch (e:Exception){
                _mensaje.value = "Error al cargar los tratamientos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarTratamientosPorCita(idCita: String) {
        viewModelScope.launch {
            _loading.value = true

            try{
                val lista = repo.obtenerTratamientosPorCita(idCita)
                _tratamientos.value = lista
                _mensaje.value = null

            } catch (e: Exception) {
                _mensaje.value = "Error al cargar los tratamientos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun crearTratamiento(tratamiento: Tratamiento){
        viewModelScope.launch {
            try{
                repo.crearTratamiento(tratamiento)
                _mensaje.value = "Tratamiento creado"
            } catch (e: Exception){
                _mensaje.value = "Error al crear el tratamiento: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun editarTratamiento(tratamiento: Tratamiento) {
        viewModelScope.launch {
            try{
                repo.editarTratamiento(tratamiento)
                _mensaje.value = "Tratamiento actualizado"
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar el tratamiento: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarTratamiento(idTratamiento: String){
        viewModelScope.launch {
            _loading.value = true
            try{
                repo.eliminarTratamiento(idTratamiento)
            }catch (e: Exception){
                _mensaje.value = "Error al eliminar tratamiento: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}