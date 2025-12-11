package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class SolicitudCita (
    val Id_solicitud: String = "",
    val Id_usuario: String = "",
    val Id_medico: String = "",
    val especialidad: String = "",
    val motivo: String = "",
    val fechaSolicitud: String = "",
    val estado: String = ""
)