package com.example.healthpoint_tfg_alejandrocolcherodefinitivo



import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



class GestionCitasViewModel: ViewModel() {


    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> get() = _citas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> get() = _mensaje


    // Carga en tiempo real
    fun cargarCitasMedico(idMedico: String) {
        _loading.value = true
        db.collection("Cita").addSnapshotListener { value, error ->
            if (error != null) {
                _mensaje.value = "Error al cargar las citas"
                _loading.value = false
                return@addSnapshotListener
            }

            if (value != null) {
                _citas.value = value.documents.mapNotNull { doc ->
                    val medicoCampo = when {
                        doc.contains("Id_medico") -> doc.getString("Id_medico")
                        doc.contains("idMedico") -> doc.getString("idMedico")
                        doc.contains("id_medico") -> doc.getString("id_medico")
                        doc.contains("Id_Medico") -> doc.getString("Id_Medico")
                        else -> null
                    }

                    if (medicoCampo != idMedico) return@mapNotNull null

                    Cita(
                        Id_cita = doc.id,
                        Id_usuario = doc.getString("Id_usuario") ?: "",
                        Id_medico = medicoCampo ?: "",
                        Id_centroMedico = doc.getString("Id_centroMedico") ?: "",
                        fecha = doc.getString("fecha") ?: "",
                        hora = doc.getString("hora") ?: "",
                        motivo = doc.getString("motivo") ?: "",
                        estado = doc.getString("estado") ?: "",
                        notasMedico = doc.getString("notasMedico") ?: ""
                    )
                }
            }
            _loading.value = false
        }
    }


    // Crear cita
    fun crearCita(cita: Cita) {
        viewModelScope.launch {
            _loading.value = true
            val referencia = db.collection("Cita").document()
            val nuevaCita = cita.copy(Id_cita = referencia.id)
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
            db.collection("Cita").document(cita.Id_cita)
                .set(cita)
                .addOnSuccessListener { _mensaje.value = "Cita actualizada" }
                .addOnFailureListener { _mensaje.value = "Error al actualizar la cita" }
            _loading.value = false
        }
    }


    // Cambiar estado
    fun cambiarEstado(idCita: String, nuevoEstado: String) {
        viewModelScope.launch {
            _loading.value = true

            db.collection("Cita").document(idCita)
                .update("estado", nuevoEstado)
                .addOnSuccessListener { _mensaje.value = "Estado de la cita actualizado" }
                .addOnFailureListener { _mensaje.value = "Error al intentar cambiar estado" }

            _loading.value = false
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