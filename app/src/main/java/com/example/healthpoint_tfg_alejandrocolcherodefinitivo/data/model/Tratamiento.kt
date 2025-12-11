package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Tratamiento(
    val Id_tratamiento: String = "",
    val Id_cita: String = "",
    val Id_usuario: String = "",
    val descripcion: String = "",
    val duracionDias: Int = 0,
    val indicaciones: String = ""
)