package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Cita(
    val Id_Cita: String = "",
    val Id_usuario: String = "",
    val Id_medico: String = "",
    val Id_centroMedico: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "Pendiente",
    val notasMedico: String = ""
)