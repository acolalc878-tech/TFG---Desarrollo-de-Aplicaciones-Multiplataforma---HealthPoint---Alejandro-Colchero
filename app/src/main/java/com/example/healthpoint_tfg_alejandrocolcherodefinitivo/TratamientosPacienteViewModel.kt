package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TratamientosPacienteViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _tratamientos = MutableStateFlow<List<Tratamiento>>(emptyList())

    private val _loading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)

    // Funcion para cargar los tratamientos que tiene el usuario
    fun cargarTratamientos(idUsuarioModelo: String?= null) {
        viewModelScope.launch{

            try {
                _loading.value = true

                val idUsuario = idUsuarioModelo ?: run {
                    val email = auth.currentUser?.email?: throw Exception("Usuario no autenticado en la base de datos")
                    val snapshot = db.collection("Usuario").whereEqualTo("email", email).limit(1)
                        .get()
                        .await()
                    if(snapshot.isEmpty) throw Exception("Usuario no encontrado en la base de datos")
                    snapshot.documents[0].getString("Id_usuario") ?: ""
                }

                val snapshot = db.collection("Tratamiento").whereEqualTo("Id_cita", idUsuario)
                    .get().await()
                _tratamientos.value= snapshot.documents.mapNotNull { doc ->
                    try{
                        Tratamiento(
                            Id_tratamiento = doc.id,
                            Id_cita = doc.getString("Id_cita") ?: "",
                            descripcion = doc.getString("descripcion") ?: "",
                            duracionDias = doc.getLong("duracionDias")?.toInt()?: 0,
                            indicaciones = doc.getString("indicaciones")?: ""
                        )
                    } catch (e: Exception){null}
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar tratamiento"
            } finally {
                _loading.value = false
            }
        }
    }
}