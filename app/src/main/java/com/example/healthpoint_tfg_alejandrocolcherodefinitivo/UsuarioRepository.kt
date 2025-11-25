package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import com.google.firebase.firestore.FirebaseFirestore

class UsuarioRepository {

    // Hacemos referencia a la base de datos y a los usuarios
    private val db = FirebaseFirestore.getInstance()
    private val usuarioCollection = db.collection("Usuario")

    fun crearUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        usuarioCollection.document(usuario.Id_usuario)
            .set(usuario)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun obtenerUsuario(idUsuario: String, callback: (Usuario?) -> Unit) {
        usuarioCollection.document(idUsuario)
            .get()
            .addOnSuccessListener { document ->
                callback(document.toObject(Usuario::class.java))
            }
            .addOnFailureListener { callback(null) }
    }

}