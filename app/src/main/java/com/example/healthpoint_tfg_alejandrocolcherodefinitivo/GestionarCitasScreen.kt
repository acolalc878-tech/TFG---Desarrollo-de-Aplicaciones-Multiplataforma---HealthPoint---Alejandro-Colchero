package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionCitasScreen(
    idMedico: String,
    onBack: () -> Unit,
    viewModel: GestionCitasViewModel = viewModel()
) {
    LaunchedEffect(idMedico) {
        viewModel.cargarCitasMedico(idMedico)
    }

    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var showCrearDialog by remember { mutableStateOf(false) }
    var showEditarDialog by remember { mutableStateOf<Cita?>(null) }
    var showEliminarDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestion de citas") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
        }
        // Botones de accion
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { showCrearDialog = true }) { Text("Crear Cita") }
            Button(onClick = { showEliminarDialog = true }) { Text("Eliminar Cita") }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Lista de citas
        Text(
            "Citas de medico",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(citas) { cita ->
                    CitaCard(
                        cita = cita,
                        onDelete = { viewModel.eliminarCita(cita.Id_cita) },
                        onEdit = { showEditarDialog = cita }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // Mensaje SnackBar
    if (mensaje.isNotEmpty()) {
        Toast.makeText(LocalContext.current, mensaje, Toast.LENGTH_SHORT).show()
    }

    // Dialogos
    if (showCrearDialog) {
        CrearCitaDialog(
            idMedico = idMedico,
            onDismiss = { showCrearDialog = false },
            onCrear = { viewModel.crearCita(it); showCrearDialog = false }
        )
    }

    showEditarDialog?.let { cita ->
        EditarCitaDialog(
            cita = cita,
            onDismiss = { showEditarDialog = null },
            onGuardar = { viewModel.editarCita(it); showEditarDialog = null }
        )
    }

    if (showEliminarDialog) {
        EliminarCitaDialog(
            citas = citas,
            onDismiss = { showEliminarDialog = false },
            onDelete = {
                viewModel.eliminarCita(it.Id_cita)
                showEliminarDialog = false
            }
        )
    }
}


@Composable
fun CitaCard(
    cita: Cita,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF3FF))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.titleMedium)
            Text("Hora: ${cita.hora}")
            Text("Motivo: ${cita.motivo}")
            Text("Estado: ${cita.estado}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                    Text("Eliminar")
                }
            }
        }
    }
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