package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Cita(
    val id_cita: String = "",
    val id_usuario: String = "",
    val id_medico: String = "",
    val Id_centroMedico: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "",
    val notasMedico: String = ""
)