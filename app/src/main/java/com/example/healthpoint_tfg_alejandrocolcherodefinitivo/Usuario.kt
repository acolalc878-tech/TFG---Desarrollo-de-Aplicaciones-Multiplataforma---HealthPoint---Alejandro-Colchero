package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Usuario(
    val Id_usuario: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val edad: Int = 0,
    val email: String = "",
    val telefono: String = "",
    val contraseña: String = "",
    val rol: String = "", // "Paciente" o "Medico"
    val fechaNacimiento: String = ""
)