package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Medico (
    val Id_medico: String = "",
    val Id_usuario: String = "", // FK a Usuario
    val Id_CentroMedico: String = "", // FK a Centro Medico
    val nombre: String = "",
    val especialidad: String = "",
    val numColegiado: String = "",
    val horario: String = ""
)