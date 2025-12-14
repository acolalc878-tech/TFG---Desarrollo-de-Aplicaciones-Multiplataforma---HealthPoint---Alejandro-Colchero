package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class CentroMedico(
    val Id_centroMedico: String = "", // PK
    val nombreCentro: String = "",
    val dirección: String = "",
    val telefono: String = "",
    val email: String = ""
)