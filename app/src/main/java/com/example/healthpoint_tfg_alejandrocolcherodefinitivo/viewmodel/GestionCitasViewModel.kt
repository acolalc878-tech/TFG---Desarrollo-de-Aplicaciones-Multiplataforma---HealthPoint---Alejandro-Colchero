package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.repository.CitasRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GestionCitasViewModel(
    private val citaRepository: CitasRepository = CitasRepository()
): ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> get() = _citas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> get() = _mensaje

    private var idMedicoActual: String = ""

    // Carga en tiempo real
    fun cargarCitasMedico(idMedico: String) {
        idMedicoActual = idMedico
        _loading.value = true

        db.collection("Cita")
            .whereEqualTo("id_medico", idMedico)
            .whereNotEqualTo("estado", "FINALIZADA")
            .addSnapshotListener { value, error ->
                _loading.value = false
                if (error != null) {
                    _mensaje.value = "Error al cargar las citas: ${error.message}"
                    return@addSnapshotListener
                }

                        if (value != null) {
                            _citas.value = value.documents.mapNotNull { doc ->
                                doc.toObject(Cita::class.java)?.copy(id_cita = doc.id)
                            }
                        }
                    }
                }

    fun cargarCitasUsuario(idPaciente: String) {
        _loading.value = true
        val idPacienteNormalizado = idPaciente.lowercase()

        db.collection("SolicitudCita")
            .whereEqualTo("id_usuario", idPacienteNormalizado)
            .addSnapshotListener { snapshot, e ->

                _loading.value = false
                if (e != null) {
                    return@addSnapshotListener
                }

                val citasActualizadas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Cita::class.java)?.copy(id_cita = doc.id)
                } ?: emptyList()

                _citas.value = citasActualizadas
            }
    }



    // Crear cita
    fun crearCita(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            val referencia = db.collection("Cita").document()
            val nuevaCita = cita.copy(id_cita = referencia.id)
            referencia.set(nuevaCita)
                .addOnSuccessListener { _mensaje.value = "Cita Creada" }
                .addOnFailureListener { _mensaje.value = "Error al crear la cita" }
            _loading.value = false
        }
    }

    // Editar la cita
    fun editarCita(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            db.collection("Cita").document(cita.id_cita)
                .set(cita)
                .addOnSuccessListener { _mensaje.value = "Cita actualizada" }
                .addOnFailureListener { _mensaje.value = "Error al actualizar la cita" }
            _loading.value = false
        }
    }

    // Cambiar estado
    fun marcarCitaFinalizada(citaId: String) {
        viewModelScope.launch {
            try {
                citaRepository.finalizarCita(citaId)
                // Recargar la lista de citas para que la interfaz se actualice
                cargarCitasMedico(idMedicoActual)
            } catch (e: Exception) {
                println("Error al finalizar la cita $citaId: ${e.message}")
            }
        }
    }

    // Eliminar
    fun eliminarCita(id: String) {
        viewModelScope.launch {
            _loading.value = true

            db.collection("Cita").document(id)
                .delete()
                .addOnSuccessListener { _mensaje.value = "Cita eliminada" }
                .addOnFailureListener { _mensaje.value = "Error al intentar eliminar el estado" }
        }
    }
}