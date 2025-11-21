package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CitasPacienteViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas = _citas

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    // Cargamos por el id del usuario, si idUsuarioModelo es nulo, se resuelve por email
    fun cargarCitasPorUsuario(idUsuarioModelo: String? = null) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val idUsuario = idUsuarioModelo ?: run {
                    // Resolucion por email
                    val email = auth.currentUser?.email ?: throw Exception("Usuario no autenticado")
                    val snapshot = db.collection("Usuario").whereEqualTo("email", email)
                        .limit(1)
                        .get()
                        .await()
                    if (snapshot.isEmpty) throw Exception("Usuario no encontrado en Firestore")
                    snapshot.documents[0].getString("Id_usuario") ?: ""
                }

                val result = db.collection("Cita")
                    .whereEqualTo("Id_usuario", idUsuario)
                    .get()
                    .await()

                val lista = result.documents.mapNotNull { doc ->
                    //Si un campo esta mal, no crashea
                    try {
                        Cita(
                            Id_Cita = doc.id,
                            Id_usuario = doc.getString("Id_usuario") ?: "",
                            Id_medico = doc.getString("Id_medico") ?: "",
                            Id_centroMedico = doc.getString("Id_centroMedico") ?: "",
                            fecha = doc.getString("fecha") ?: "",
                            hora = doc.getString("hora") ?: "",
                            motivo = doc.getString("motivo") ?: "",
                            estado = doc.getString("estado") ?: "",
                            notasMedico = doc.getString("notasMedico") ?: ""
                        )
                    } catch (e: Exception) {null}
                }
                _citas.value = lista
            } catch (e: Exception) {
                _error.value = "Error al cargar las citas ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}