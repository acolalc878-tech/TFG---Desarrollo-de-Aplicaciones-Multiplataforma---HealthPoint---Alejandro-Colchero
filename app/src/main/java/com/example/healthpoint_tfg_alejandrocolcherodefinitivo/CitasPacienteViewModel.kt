package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CitasPacienteViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Implementar la carga de citas del médico aquí (similar a CitasPacienteViewModel)
    fun cargarCitasPorUsuario(idUsuarioModelo: String? = null) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val idUsuario = idUsuarioModelo ?: run {
                    val email = auth.currentUser?.email ?: throw Exception("Usuario no autenticado")
                    val snapshot = db.collection("Usuario").whereEqualTo("email", email)
                        .limit(1)
                        .get()
                        .await()
                    if (snapshot.isEmpty) throw Exception("Usuario no encontrado en Firestore")
                    snapshot.documents[0].getString("Id_usuario") ?: ""
                }

                db.collection("Cita")
                    .whereEqualTo("Id_paciente", idUsuario) // ⬅️ Usar Id_paciente
                    .addSnapshotListener { value, error ->

                        if (error != null) {
                            _error.value = "Error: ${error.message}"
                            _loading.value = false
                            return@addSnapshotListener
                        }

                        if (value != null) {
                            val lista = value.documents.mapNotNull { it.toObject(Cita::class.java) }
                            _citas.value = lista
                            _loading.value = false
                        }
                    }

            } catch (e: Exception) {
                _error.value = "Error al cargar citas: ${e.message}"
                _loading.value = false
            }
        }
    }
}
