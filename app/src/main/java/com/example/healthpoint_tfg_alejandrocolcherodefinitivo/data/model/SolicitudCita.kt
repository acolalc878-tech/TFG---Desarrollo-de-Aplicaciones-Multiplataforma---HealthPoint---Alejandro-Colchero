package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class SolicitudCita (
    val id_solicitud: String = "", // PK
    val id_usuario: String = "", // FK a Usuario
    val id_medico: String = "",  // FK a Medico
    val especialidad: String = "",
    val motivo: String = "",
    val estado: String = "",
    val fechaSolicitud: String = ""
)