package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Paciente(
    val Id_paciente: String = "", // PK
    val Id_Usuario: String = "", // FK a Usuario
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val telefono: String = "",
    val dni: String = "",
    val fechaNacimiento: String = ""
)