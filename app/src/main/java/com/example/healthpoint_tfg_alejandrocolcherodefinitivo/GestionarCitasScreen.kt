package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarCitasScreen(
    idMedico: String,
    onBack: () -> Unit,
    viewModel: GestionCitasViewModel = viewModel()
) {

    LaunchedEffect(idMedico) {
        viewModel.cargarCitasMedico(idMedico)
    }

    val citas by viewModel.citas.collectAsState()

    var showCrearDialog by remember { mutableStateOf(false) }
    var citaAEditar by remember { mutableStateOf<Cita?>(null) }
    var citaAEliminar by remember { mutableStateOf<Cita?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
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
                    CitasCardScreen(
                        cita = cita,
                        onEdit = { citaAEditar = cita },
                        onDelete = { citaAEliminar = cita }
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
                Id_usuario = "",
                Id_medico = idMedico,
                Id_centroMedico = "",
                fecha = "",
                hora = "",
                motivo = "",
                estado = "Pendiente",
                notasMedico = ""
            ),
            onDismiss = { showCrearDialog = false },
            onSubmit = { nuevaCita ->
                viewModel.crearCita(nuevaCita)
                showCrearDialog = false
            }
        )
    }

    // DIALOG EDITAR
    citaAEditar?.let { cita ->
        FormCitaDialog(
            titulo = "Editar cita",
            citaInicial = cita,
            onDismiss = { citaAEditar = null },
            onSubmit = { citaEditada ->
                viewModel.editarCita(citaEditada)
                citaAEditar = null
            }
        )
    }

    // DIALOG ELIMINAR
    citaAEliminar?.let { cita ->
        ConfirmarEliminarDialog(
            onDismiss = { citaAEliminar = null },
            onConfirm = {
                viewModel.eliminarCita(cita.Id_cita)
                citaAEliminar = null
            }
        )
    }
}


@Composable
fun CitasCardScreen(
    cita: Cita,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text("Fecha: ${cita.fecha}")
            Text("Hora: ${cita.hora}")
            Text("Motivo: ${cita.motivo}")
            Text("Estado: ${cita.estado}")

            Spacer(Modifier.height(16.dp))

            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onEdit
                ) { Text("Editar") }

                Spacer(Modifier.width(8.dp))

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onDelete
                ) { Text("Borrar") }
            }
        }
    }
}

@Composable
fun FormCitaDialog(
    titulo: String,
    citaInicial: Cita,
    onDismiss: () -> Unit,
    onSubmit: (Cita) -> Unit
) {
    var idUsuario by remember { mutableStateOf(citaInicial.Id_usuario) }
    var idCentro by remember { mutableStateOf(citaInicial.Id_centroMedico) }
    var fecha by remember { mutableStateOf(citaInicial.fecha) }
    var hora by remember { mutableStateOf(citaInicial.hora) }
    var motivo by remember { mutableStateOf(citaInicial.motivo) }
    var estado by remember { mutableStateOf(citaInicial.estado) }
    var notas by remember { mutableStateOf(citaInicial.notasMedico) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column {

                OutlinedTextField(
                    value = idUsuario,
                    onValueChange = { idUsuario = it },
                    label = { Text("ID Usuario") }
                )

                OutlinedTextField(
                    value = idCentro,
                    onValueChange = { idCentro = it },
                    label = { Text("ID Centro médico") }
                )

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") }
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora") }
                )

                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo") }
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") }
                )

                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas del médico") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSubmit(
                    citaInicial.copy(
                        Id_usuario = idUsuario,
                        Id_centroMedico = idCentro,
                        fecha = fecha,
                        hora = hora,
                        motivo = motivo,
                        estado = estado,
                        notasMedico = notas
                    )
                )
            }) {
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
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar cita") },
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


@Composable
fun CrearCitaDialog(
    idMedico: String,
    onDismiss: () -> Unit,
    onCrear: (Cita) -> Unit
) {
    var fecha by remember { mutableStateOf("")}
    var hora by remember { mutableStateOf("")}
    var motivo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear nueva cita")},
        text = {
            Column{
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") })
                OutlinedTextField(value = hora, onValueChange = { fecha = it }, label = { Text("Hora") })
                OutlinedTextField(value = motivo, onValueChange = { fecha = it }, label = { Text("Motivo") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onCrear(
                    Cita(
                        Id_cita = "",
                        Id_usuario = "SIN ASIGNAR",
                        Id_medico = idMedico,
                        Id_centroMedico = "centro001",
                        fecha = fecha,
                        hora = hora,
                        motivo = motivo,
                        estado = "Pendiente",
                        notasMedico = ""
                    )
                )
            }) {
                Text("Crear")
            }
        }
    )
}

@Composable
fun EditarCitaDialog(
    cita: Cita,
    onDismiss: () -> Unit,
    onGuardar:(Cita) -> Unit
) {
    var fecha by remember { mutableStateOf(cita.fecha)}
    var hora by remember { mutableStateOf(cita.hora)}
    var motivo by remember { mutableStateOf(cita.motivo) }
    var estado by remember { mutableStateOf(cita.estado)}

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Editar Cita")},
        text = {
            Column {
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") })
                OutlinedTextField(value = hora, onValueChange = { hora = it }, label = { Text("Hora") })
                OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo") })

                Spacer(Modifier.height(8.dp))
                Text("Estado:")
                DropdownMenuEstado(estado) { estado = it}
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(
                    cita.copy(
                        fecha = fecha,
                        hora = hora,
                        motivo = motivo,
                        estado = estado
                    )
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar")} }
    )
}

@Composable
fun EliminarCitaDialog(
    cita: Cita,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar cita")},
        text = {Text("¿Seguro que quiereseliminar esta cita del día ${cita.fecha}?")},
        confirmButton = {
            Button(onClick = onDelete) { Text("Eliminar")}
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }    )
}


@Composable
fun DropdownMenuEstado(
    estadoActual: String,
    onChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var estados = listOf("Pendiente", "Confirmada", "Cancelada", "Realizada")

    Box{
        Button(onClick = {expanded = true}) {
            Text(estadoActual)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            estados.forEach{ estado ->
                DropdownMenuItem(
                    text = {Text(estado)},
                    onClick = {
                        onChange(estado)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EliminarCitaDialog(
    citas : List<Cita>,
    onDismiss: () -> Unit,
    onDelete: (Cita) -> Unit
) {
    var seleccion by remember{ mutableStateOf<Cita?>(null)}

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Cita")},
        text = {
            Column {
                Text("Selecciona una cita para eliminar: ")

                LazyColumn {
                    items(citas) {cita ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { seleccion = cita }
                                .padding(8.dp)
                        ) {
                            RadioButton(
                                selected = seleccion == cita,
                                onClick = {seleccion = cita}
                            )
                            Text("${cita.fecha} - ${cita.hora} (${cita.motivo})")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { seleccion?.let { onDelete(it) } }) {
                Text("Eliminar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar")} }
    )
}