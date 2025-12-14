package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Medico
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.SolicitarCitasViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudCitaScreen(
    idPaciente: String,
    onBack: () -> Unit,
    onSolicitudEnviada: (String) -> Unit,
    viewModel: SolicitarCitasViewModel = viewModel()
) {
    val especialidades by viewModel.especialidades.collectAsState()
    val medicos by viewModel.medicos.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var especialidadSel by remember { mutableStateOf("") }
    var medicoSel by remember { mutableStateOf<Medico?>(null) }
    var motivo by remember { mutableStateOf("") }

    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        viewModel.cargarEspecialidades()
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FFC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Solicitar Cita",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {

            Text(
                "Completa los datos para enviar tu solicitud de cita médica.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(24.dp))

            DropdownMenuEspecialidad(
                especialidades = especialidades,
                seleccionada = especialidadSel,
                onSeleccion = { especialidad ->
                    especialidadSel = especialidad
                    viewModel.cargarMedicos(especialidad)
                    medicoSel = null
                    viewModel.limpiarMensaje()
                }
            )

            Spacer(Modifier.height(12.dp))

            // Medicos segun su especialidad
            DropdownMenuMedicos(
                medicos = medicos,
                seleccionada = medicoSel,
                onSeleccion = { medico ->
                    medicoSel = medico
                    viewModel.limpiarMensaje()
                }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo de la consulta") },
                placeholder = {Text("Describa brevemente el motivo de su consulta")},
                singleLine = false,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // Boton de Enviar
            Button(
                onClick = {
                    medicoSel?.let {
                        viewModel.enviarSolicitud(idPaciente, it, motivo)

                        onSolicitudEnviada(idPaciente)
                        // Cuando enviamos reiniciamos el motivo
                        motivo = ""
                        especialidadSel = "" // Recargamos especialidad tambien
                        medicoSel = null
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = medicoSel != null && motivo.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Enviar Solicitud", style = MaterialTheme.typography.titleMedium)
            }

            // Mensaje de respuesta
            mensaje?.let { mensaje ->
                Spacer(Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = primaryColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Estado",
                            tint = primaryColor
                        )

                        Spacer(Modifier.width(8.dp))
                        Text(
                            mensaje,
                            color = primaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuEspecialidad(
    especialidades: List<String>,
    seleccionada: String,
    onSeleccion: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (seleccionada.isEmpty()) "Seleccione una especialidad del medico que quiere que le atienda"
            else seleccionada,
            onValueChange = {},
            readOnly = true,
            label = { Text("Especialidad") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            especialidades.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSeleccion(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuMedicos(
    medicos: List<Medico>,
    seleccionada: Medico?,
    onSeleccion: (Medico) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val nombreDisplay = seleccionada?.let { "${it.nombre} ${it.apellidos}" } ?: "Selecciona médico"
    val enabled = medicos.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { expanded = !expanded && enabled }
    ) {
        OutlinedTextField(
            value = nombreDisplay,
            onValueChange = {},
            readOnly = true,
            label = { Text("Médico (por especialidad)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            enabled = enabled,
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            medicos.forEach { medico ->
                DropdownMenuItem(
                    text = { Text("${medico.nombre} ${medico.apellidos} - Col. ${medico.numColegiado}") },
                    onClick = {
                        onSeleccion(medico)
                        expanded = false
                    }
                )
            }
        }
    }
}

