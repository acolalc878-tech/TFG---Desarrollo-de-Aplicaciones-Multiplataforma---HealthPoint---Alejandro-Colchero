package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Tratamiento(
    val Id_tratamiento: String = "", // PK
    val Id_cita: String = "", // FK a Cita
    val Id_usuario: String = "", // FK a Usuario
    val descripcion: String = "",
    val duracionDias: Int = 0,
    val indicaciones: String = ""
)