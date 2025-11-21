package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerfilViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth= FirebaseAuth.getInstance()

    val usuario= MutableStateFlow<Usuario?>(null)
    val loading = MutableStateFlow(false)
    val mensajeGuardado = MutableStateFlow<String?>(null)

    fun cargarPerfil() {
        viewModelScope.launch {
            try{
                loading.value = true
                val email = auth.currentUser?.email ?: throw Exception("Usuario no autenticado en la base de datos")

                val snapshot = db.collection("Usuario").whereEqualTo("email", email).limit(1).get()
                    .await()

                if (snapshot.isEmpty) throw Exception("Usuario no encontrado")

                val document = snapshot.documents[0]
                usuario.value = document.toObject(Usuario::class.java)
            }catch (e:Exception){
                mensajeGuardado.value = e.message
            } finally {
                loading.value = false
            }
        }
    }

    fun guardarCambiosPerfil(nuevosDatos: Usuario){
        viewModelScope.launch {
            try{
                loading.value = true

                val snapshot = db.collection("Usuario").whereEqualTo("email", nuevosDatos.email.trim()).limit(1)
                    .get()
                    .await()
                if(snapshot.isEmpty) throw Exception("Usuario no encontrado")
            }
        }
    }
}