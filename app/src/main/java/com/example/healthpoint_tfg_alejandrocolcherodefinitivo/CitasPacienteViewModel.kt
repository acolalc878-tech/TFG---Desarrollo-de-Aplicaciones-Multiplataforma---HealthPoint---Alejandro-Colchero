package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CitasPacienteViewModel: ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> = _citas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarCitaPacientes(){
        val userId = auth.currentUser?.uid?: return

        viewModelScope.launch {
            try{
                _loading.value = true

                val result = db.collection("citas")
                    .whereEqualTo("id_usuario", userId)
                    .get()
                    .await()

                val lista = result.documents.map { doc ->
                    Cita(
                        id = doc.id,
                        id_usuario = doc.getString("id_usuario") ?: "",
                        id_medico = doc.getString("id_medico")?: "",
                        id_centroMedico = doc.getString("id_centroMedico")?: "",
                        fecha = doc.getString("fecha")?: "",
                        hora = doc.getString("hora")?: "",
                        motivo = doc.getString("motivo")?: "",
                        estado = doc.getString("estado")?: "",
                        notasMedico = doc.getString("notasMedico")?: ""
                    )
                }

                _citas.value = lista
            } catch (e: Exception){
                _error.value = "Error al cargar las citas del usuario ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}