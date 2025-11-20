package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val uid = auth.currentUser!!.uid

                db.collection("Usuario").document(uid).get()
                    .addOnSuccessListener { document ->
                        val rol = document.getString("rol")

                        if (rol != null) {
                            onLoginSuccess(rol)
                        } else {
                            onError("No se encontró el rol del usuario")
                        }
                    }
                    .addOnFailureListener { e ->
                        onError("Error al obtener datos: ${e.message}")
                    }

            } else {
                onError("Error al iniciar sesión: ${task.exception?.message}")
            }
        }
    }
}