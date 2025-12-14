package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model

data class SolicitudCita (
    var Id_solicitud: String = "", // PK
    var Id_usuario: String = "", // FK a Usuario
    var Id_medico: String = "",  // FK a Medico
    var especialidad: String = "",
    var motivo: String = "",
    var estado: String = "",
    var fechaSolicitud: String = ""
)