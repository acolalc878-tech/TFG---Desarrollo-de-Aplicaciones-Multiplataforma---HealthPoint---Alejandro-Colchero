package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Medico (
    val Id_medico: String = "", // PK
    val Id_usuario: String = "", // FK a Usuario
    val Id_CentroMedico: String = "", // FK a Centro Medico
    val nombre: String = "",
    val apellidos: String = "",
    val especialidad: String = "",
    val horario: String = "",
    val numColegiado: String = ""
)