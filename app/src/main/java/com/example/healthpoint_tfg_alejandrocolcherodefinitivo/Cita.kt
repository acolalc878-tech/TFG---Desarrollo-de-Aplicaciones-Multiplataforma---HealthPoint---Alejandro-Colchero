package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Cita(
    val id: String = "",
    val id_usuario: String = "",
    val id_medico: String = "",
    val id_centroMedico: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "",
    val notasMedico: String = ""
)