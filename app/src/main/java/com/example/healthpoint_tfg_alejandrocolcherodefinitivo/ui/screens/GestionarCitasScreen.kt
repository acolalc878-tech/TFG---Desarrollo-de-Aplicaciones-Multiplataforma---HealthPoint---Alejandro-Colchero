package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.GestionCitasViewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.GestionDatosComunesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarCitasScreen(
    idMedico: String, onBack: () -> Unit,
    viewModel: GestionCitasViewModel = viewModel(),
    comunesViewModel: GestionDatosComunesViewModel = viewModel()
) {

    val citas by viewModel.citas.collectAsState()
    var showCrearDialog by remember { mutableStateOf(false) }
    var citaAEditar by remember { mutableStateOf<Cita?>(null) }
    var citaAEliminar by remember { mutableStateOf<Cita?>(null) }

    val listaPacientes by comunesViewModel.pacientes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarCitasMedico(idMedico)
        comunesViewModel.cargarTodosLosPacientes()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Gestión de citas") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
        })
    }

    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Botón crear cita
            Button(onClick = { showCrearDialog = true }) {
                Text("Crear nueva cita")
            }

            Spacer(Modifier.height(16.dp))

            if (citas.isEmpty()) {
                Text("No hay citas registradas")
            } else {
                citas.forEach { cita ->
                    CitasCardScreen(cita = cita,
                        onEdit = { citaAEditar = cita },
                        onDelete = { citaAEliminar = cita },
                        onFinalizar = { viewModel.marcarCitaFinalizada(cita.id_cita) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }

    // DIALOG CREAR
    if (showCrearDialog) {
        FormCitaDialog(
            titulo = "Crear nueva cita",
            citaInicial = Cita(
                id_usuario = "",
                id_medico = idMedico,
                Id_centroMedico = "",
                fecha = "",
                hora = "",
                motivo = "",
                estado = "Pendiente",
                notasMedico = ""
        ),
            pacientesDisponibles = listaPacientes,
            onDismiss = { showCrearDialog = false }, onSubmit = { nuevaCita ->
            viewModel.crearCita(nuevaCita)
            showCrearDialog = false
        })
    }

    // DIALOG EDITAR
    citaAEditar?.let { cita ->
        FormCitaDialog(titulo = "Editar cita",
            citaInicial = cita,
            onDismiss = { citaAEditar = null },
            pacientesDisponibles = listaPacientes,
            onSubmit = { citaEditada ->
                viewModel.editarCita(citaEditada)
                citaAEditar = null
            })
    }

    // DIALOG ELIMINAR
    citaAEliminar?.let { cita ->
        ConfirmarEliminarDialog(onDismiss = { citaAEliminar = null }, onConfirm = {
            viewModel.eliminarCita(cita.id_cita)
            citaAEliminar = null
        })
    }
}

@Composable
fun CitasCardScreen(
    cita: Cita,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onFinalizar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text("Fecha: ${cita.fecha}")
            Text("Hora: ${cita.hora}")
            Text("Motivo: ${cita.motivo}")

            val estadoColor = when (cita.estado.uppercase()){
                "PENDIENTE" -> MaterialTheme.colorScheme.primary
                "FINALIZADA" -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.onSurface
            }
            Text("Estado: ${cita.estado}", color = estadoColor)

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {

                if(cita.estado.uppercase() == "PENDIENTE") {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onFinalizar,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Text("Finalizar")
                    }
                    Spacer(Modifier.width(8.dp))
                }

                // Botones de Editar y Borrar
                Button(
                    modifier = Modifier.weight(1f), onClick = onEdit
                ) { Text("Editar") }

                Spacer(Modifier.width(8.dp))

                Button(
                    modifier = Modifier.weight(1f), onClick = onDelete
                ) { Text("Borrar") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCitaDialog(
    titulo: String,
    citaInicial: Cita,
    pacientesDisponibles: List<Usuario>,
    onDismiss: () -> Unit,
    onSubmit: (Cita) -> Unit
) {
    var idPacienteSeleccionado by remember { mutableStateOf(citaInicial.id_usuario) }
    var expandedPaciente by remember { mutableStateOf(false) }

    var idCentro by remember { mutableStateOf(citaInicial.Id_centroMedico) }
    var fecha by remember { mutableStateOf(citaInicial.fecha) }
    var hora by remember { mutableStateOf(citaInicial.hora) }
    var motivo by remember { mutableStateOf(citaInicial.motivo) }
    var estado by remember { mutableStateOf(citaInicial.estado) }
    var notas by remember { mutableStateOf(citaInicial.notasMedico) }

    val pacienteActual = pacientesDisponibles.find { it.nombre == idPacienteSeleccionado }
    val pacienteDisplay = if (pacienteActual != null) {
        "${pacienteActual.nombre} ${pacienteActual.apellidos}"
    } else {
        "Seleccionar Paciente..."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column {

                Box(Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expandedPaciente,
                        onExpandedChange = {expandedPaciente = !expandedPaciente }
                    ) {
                        OutlinedTextField(
                            value = pacienteDisplay,
                            onValueChange = { /*solo lectura*/ },
                            readOnly = true,
                            label = { Text("Paciente") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaciente) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedPaciente,
                            onDismissRequest = {expandedPaciente = false}
                        ) {
                            pacientesDisponibles.forEach { paciente ->
                                DropdownMenuItem(
                                    text = { Text("${paciente.nombre} ${paciente.apellidos}") },
                                    onClick = {
                                        idPacienteSeleccionado = paciente.Id_usuario
                                        expandedPaciente = false
                                    })
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = idCentro,
                    onValueChange = { idCentro = it },
                    label = { Text("ID Centro médico") })

                OutlinedTextField(value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") })

                OutlinedTextField(value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora") })

                OutlinedTextField(value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo") })

                OutlinedTextField(value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") })

                OutlinedTextField(value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas del médico") })
        }
    },

        confirmButton = {
            Button(onClick = {
                onSubmit(
                    citaInicial.copy(
                        id_usuario = idPacienteSeleccionado,
                        Id_centroMedico = idCentro,
                        fecha = fecha,
                        hora = hora,
                        motivo = motivo,
                        estado = estado,
                        notasMedico = notas
                    )
                )
            },
                enabled = idPacienteSeleccionado.isNotEmpty()
                ) {
                Text("Guardar")
            }
        },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
        }
    )
}


@Composable
fun ConfirmarEliminarDialog(
    onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Eliminar cita") },
        text = { Text("¿Seguro que quieres eliminar esta cita?") },

        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}