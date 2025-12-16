package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.SolicitarCitasViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudCitaScreen(
    idPaciente: String,
    onBack: () -> Unit,
    viewModel: SolicitarCitasViewModel = viewModel(),
    onSolicitudEnviada: (String) -> Unit
) {

    // Observacion de estados
    val especialidades by viewModel.especialidades.collectAsState()
    val medicos by viewModel.medicos.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Estados locales para la seleccion
    var especialidadSeleccionada by remember { mutableStateOf("") }
    var medicoSeleccionado by remember { mutableStateOf("") }
    var motivoConsulta by remember { mutableStateOf("") }

    // Estados para lod DropdowsMenus
    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedMedico by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    // Cargamos especialidades al incio
    LaunchedEffect(especialidades) {
        viewModel.cargarEspecialidades()
    }

    LaunchedEffect(mensaje) {
        mensaje?.let{ msg ->
            if(msg.contains("exito|")) {
                val partes = msg.split("|")
                val idSolicitud = partes.getOrNull(1)

                if (idSolicitud != null) {
                    onSolicitudEnviada(idSolicitud)
                    viewModel.limpiarMensaje()
                }
            }
        }
    }

    // Cargamos medicos cuando cambia la especialidad
    LaunchedEffect(especialidadSeleccionada) {
        if(especialidadSeleccionada.isNotBlank()){
            viewModel.cargarMedicos(especialidadSeleccionada)
        }
        medicoSeleccionado = "" // Reseteamos el medico si cambia la especialidad
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC), // Fondo blanco suave
        topBar = {
            TopAppBar(
                title = { Text("Solicitar Cita", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onPrimaryColor)},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = onPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor, // Azul cielo
                    titleContentColor = onPrimaryColor
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(20.dp),
        ) {
            Text(
                "Completa los datos para enviar tu solicitud de cita médica.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Seleccion de especialidad
            Box(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedEspecialidad,
                    onExpandedChange = {expandedEspecialidad = !expandedEspecialidad}
                ) {
                    OutlinedTextField(
                        value = especialidadSeleccionada.ifBlank { "Seleccione una especialidad" },
                        onValueChange = { /* Solo lectura */ },
                        readOnly = true,
                        label = { Text("Especialidad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEspecialidad,
                        onDismissRequest = { expandedEspecialidad = false}
                    ) {
                        especialidades.forEach { especialidad ->
                            DropdownMenuItem(
                                text = { Text(especialidad)},
                                onClick = {
                                    especialidadSeleccionada = especialidad
                                    expandedEspecialidad = false
                                }
                            )
                        }
                    }
                }
            }

            // Seleccion de especialidad
            Box(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedMedico,
                    onExpandedChange = {expandedMedico = !expandedMedico}
                ) {
                    OutlinedTextField(
                        value = medicos.find { it.Id_medico == medicoSeleccionado }?.let { "${it.nombre} ${it.apellidos}" }
                            ?: "Seleccione un médico",
                        onValueChange = { /* Solo lectura */ },
                        readOnly = true,
                        label = { Text("Medico (por especialidad)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMedico) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        enabled = medicos.isNotEmpty()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedMedico,
                        onDismissRequest = {expandedMedico = false}
                    ) {
                        medicos.forEach { medico ->
                            DropdownMenuItem(
                                text = { Text("${medico.nombre} ${medico.apellidos}") },
                                onClick = {
                                    medicoSeleccionado = medico.Id_medico
                                    expandedMedico = false
                                }
                            )
                        }
                    }
                }
            }

            // Motivo de la consulta
            OutlinedTextField(
                value = motivoConsulta,
                onValueChange = { motivoConsulta = it },
                label = { Text("Motivo de la consulta") },
                placeholder = {Text("Describa brevemente el motivo...")},
                singleLine = false,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp).padding(bottom = 24.dp)
            )

            // Boton de Enviar
            Button(
                onClick = {
                    val medicoElegido = medicos.find { it.Id_medico == medicoSeleccionado }
                    if (medicoElegido != null && motivoConsulta.isNotBlank()) {
                        viewModel.enviarSolicitud(idPaciente, medicoElegido, motivoConsulta)
                    } else {
                        viewModel.limpiarMensaje()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = medicoSeleccionado.isNotBlank() && motivoConsulta.isNotBlank() && !loading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if(loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))

                } else {
                    Text("Enviar solicitud", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Mensaje de respuesta
            mensaje?.let { mensaje ->
                Spacer(Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        if (mensaje.contains("éxito")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Estado",
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            mensaje,
                            color = if (mensaje.contains("éxito")) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}