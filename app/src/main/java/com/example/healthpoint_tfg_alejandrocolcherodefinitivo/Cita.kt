package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

data class Cita(
    val Id_Cita: String = "",
    val Id_usuario: String = "", // Paciente
    val Id_medico: String = "", // Medico
    val Id_centroMedico: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "",  // ENUM: Pendiente/Realizada/Cancelada
    val notasMedico: String = ""
)