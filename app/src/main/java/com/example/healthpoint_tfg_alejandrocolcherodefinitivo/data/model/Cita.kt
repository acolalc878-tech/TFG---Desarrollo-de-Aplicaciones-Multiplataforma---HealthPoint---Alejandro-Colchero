package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class Cita(
    val id_cita: String = "", // PK
    val id_usuario: String = "", // FK a Usuario
    val id_medico: String = "",  // FK a Medico
    val Id_centroMedico: String = "", // FK a Centro Medico
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "", // Pendiente - Finalizada
    val notasMedico: String = ""
)