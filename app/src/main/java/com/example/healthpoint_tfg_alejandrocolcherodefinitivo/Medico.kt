package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Medico (
    val Id_medico: String = "",
    val nombre: String = "",
    val edad: String = "",
    val apellidos: String = "",
    val Id_usuario: String = "", // FK -> Usuario.Id_usuario
    val Id_centroMedico: String = "", // FK -> CentroMedico.Id_centroMedico
    val especialidad: String = "",
    val numColegiado: String = "",
    val horario: String = ""
)