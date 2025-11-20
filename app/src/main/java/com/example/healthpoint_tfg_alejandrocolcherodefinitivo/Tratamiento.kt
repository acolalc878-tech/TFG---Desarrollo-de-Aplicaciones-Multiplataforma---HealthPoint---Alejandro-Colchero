package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Tratamiento(
    val id_tratamiento: String = "",
    val id_cita: String = "",
    val descripcion: String = "",
    val duracionDias: Int = 0,
    val indicaciones: String = ""
)