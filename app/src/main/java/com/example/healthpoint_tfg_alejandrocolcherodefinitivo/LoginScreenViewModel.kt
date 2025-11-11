package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loading = MutableLiveData(false)
    val selectedRole = MutableLiveData("Paciente") // Medico o Paciente

    fun selectedRole(role: String){
        selectedRole.value = role
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit) =
        viewModelScope.launch {
            try {
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        Log.d("Funciona correctamente", "signInWithEmailAndPassword logueado!!!")
                        home()
                    }
                    else{
                        Log.d("ERROR", "signInWithEmailAndPassword: ${task.result.toString()}!!!")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
}