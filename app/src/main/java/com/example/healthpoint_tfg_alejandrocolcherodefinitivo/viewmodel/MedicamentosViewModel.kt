package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.remote.CimaApi
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.remote.MedicamentoCIMA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicamentosViewModel : ViewModel() {

    private val _sentencia = MutableStateFlow("")
    val sentencia = _sentencia.asStateFlow()

    private val _medicamentoDetalle = MutableStateFlow<MedicamentoCIMA?>(null)
    val medicamentoDetalle = _medicamentoDetalle.asStateFlow()

    private val _resultados = MutableStateFlow<List<String>>(emptyList())
    val resultados = _resultados.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun actualizarQuery(q: String) {
        _sentencia.value = q
    }

    fun buscarSugerencia() {
        val query = _sentencia.value.trim()

        if (query.length < 3) {
            _resultados.value = emptyList()
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val respuesta = CimaApi.service.buscarMedicamentos(query)

                if (respuesta.isSuccessful && respuesta.body() != null) {

                    val lista = respuesta.body()!!.resultados

                    // Convertimos lista de objetos a lista de nombres
                    _resultados.value = lista.map { it.nombre.toString() }

                } else {
                    _error.value = "Error API: ${respuesta.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}