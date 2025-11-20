package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Tratamiento(
    val Id_tratamiento: String = "",
    val Id_cita: String = "",
    val descripcion: String = "",
    val duracionDias: Int = 0,
    val indicaciones: String = ""
)