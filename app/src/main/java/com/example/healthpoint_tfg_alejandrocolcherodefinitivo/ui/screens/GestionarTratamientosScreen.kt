package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.GestionDatosComunesViewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.TratamientosMedicoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarTratamientosScreen(
    idMedico: String,
    onBack: () -> Unit,
    viewModel: TratamientosMedicoViewModel = viewModel(),
    comunesViewModel: GestionDatosComunesViewModel = viewModel()
) {

    LaunchedEffect(idMedico) {
        viewModel.cargarTratamientos(idMedico)
        comunesViewModel.cargarCitasMedico(idMedico)
    }

    val lista by viewModel.tratamientos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    val citasDisponibles by comunesViewModel.citasMedico.collectAsState()

    var mostrarCrear by remember { mutableStateOf(false) }
    var editarSeleccionado by remember { mutableStateOf<Tratamiento?>(null) }
    var eliminarSeleccionado by remember { mutableStateOf<Tratamiento?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Tratamientos") } ,
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ){

            Button(onClick = {mostrarCrear = true}, modifier = Modifier.fillMaxWidth()) {
                Text("Crear Tratamiento")
            }

            Spacer(Modifier.height(12.dp))

            if(loading){
                CircularProgressIndicator()
            }

            if(!mensaje.isNullOrEmpty()){
                Text(mensaje ?: "", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(12.dp))

            if(lista.isEmpty()) {
                Text("No hay tratamientos", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(lista) {tratamiento ->
                        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                            Column(Modifier.padding(12.dp)) {
                                Text(tratamiento.descripcion, style = MaterialTheme.typography.titleMedium)
                                Text("Duración (días): ${tratamiento.duracionDias}")
                                Text("Indicaciones: ${tratamiento.indicaciones}")
                                Spacer(Modifier.height(8.dp))
                                RowButtonsTratamiento(
                                    onEdit = { editarSeleccionado = tratamiento},
                                    onDelete = { eliminarSeleccionado = tratamiento}
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    if (mostrarCrear) {
        DialogTratamiento(
            titulo = "Crear Tratamiento",
            tratamientoInicial = Tratamiento(),
            citasDisponibles = citasDisponibles,
            onDissmiss = { mostrarCrear = false },
            onSubmit = { nuevo ->
                viewModel.crearTratamiento(nuevo)
                mostrarCrear = false
            }
        )
    }

    editarSeleccionado?.let { t ->
        DialogTratamiento(
            titulo = "Editar Tratamiento",
            tratamientoInicial = t,
            citasDisponibles = citasDisponibles,
            onDissmiss = { editarSeleccionado = null},
            onSubmit = { actualizado ->
                viewModel.editarTratamiento(actualizado)
                editarSeleccionado = null
            }
        )
    }

    eliminarSeleccionado?.let { t ->
        DialogConfirmarEliminar(
            texto = "Eliminar tratamiento: ${t.descripcion}?",
            onDissmiss = { eliminarSeleccionado = null},
            onConfirm = {
                viewModel.eliminarTratamiento(t.Id_tratamiento)
                eliminarSeleccionado = null
            }
        )
    }
}

@Composable
fun RowButtonsTratamiento(onEdit: () -> Unit, onDelete: () -> Unit){
    Row {
        Button(onClick = onEdit, modifier = Modifier.weight(1f)){ Text("Editar") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onDelete, modifier = Modifier.weight(1f)){ Text("Eliminar") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTratamiento(
    titulo: String,
    tratamientoInicial: Tratamiento,
    citasDisponibles: List<Cita>,
    onDissmiss: () -> Unit,
    onSubmit: (Tratamiento) -> Unit
) {
    val descripcion = remember { mutableStateOf(tratamientoInicial.descripcion)}
    val duracion = remember { mutableStateOf(tratamientoInicial.duracionDias.toString())}
    val indicaciones = remember { mutableStateOf(tratamientoInicial.indicaciones)}

    var idCitaSeleccionada by remember { mutableStateOf(tratamientoInicial.Id_cita) }
    var expanded by remember { mutableStateOf(false) }

    // Funcion auxiliar que nos va a ayudar a mostrar la cita actualde forma legible
    val citaActual = citasDisponibles.find { it.id_cita == idCitaSeleccionada }
    val citaDisplay = if (citaActual != null) {
        "${citaActual.fecha} ${citaActual.hora} - P: ${citaActual.id_usuario.take(4)}..."
    } else {
        "Seleccionar Cita..."
    }

    AlertDialog(
        onDismissRequest = onDissmiss,
        title = { Text(titulo) },
        text = {
            Column {
                Box(Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {expanded = !expanded}
                    ) {
                        OutlinedTextField(
                            value = citaDisplay,
                            onValueChange = { /*Solo lectura*/ },
                            readOnly = true,
                            label = { Text("Cita para asignar Tratamiento") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {expanded = false}
                        ) {
                            citasDisponibles.forEach { cita ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${cita.fecha} ${cita.hora} (P: ${cita.id_usuario.take(4)}...)")
                                    },
                                    onClick = {
                                        idCitaSeleccionada = cita.id_cita
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                }
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(value = descripcion.value, onValueChange = { descripcion.value = it }, label = { Text("Descripción") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = duracion.value, onValueChange = { duracion.value = it }, label = { Text("Duración (días)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = indicaciones.value, onValueChange = { indicaciones.value = it }, label = { Text("Indicaciones") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSubmit(
                    tratamientoInicial.copy(
                        Id_cita = idCitaSeleccionada,
                        descripcion = descripcion.value,
                        duracionDias = duracion.value.toIntOrNull() ?: 0,
                        indicaciones = indicaciones.value
                    )
                )
            },
                enabled = idCitaSeleccionada.isNotEmpty() && descripcion.value.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDissmiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogConfirmarEliminar(texto: String, onDissmiss: () -> Unit, onConfirm: () -> Unit){
    AlertDialog(
        onDismissRequest = onDissmiss,
        title = { Text("Confirmar")},
        text = {Text(texto)},
        confirmButton = {
            Button(onClick = onConfirm) { Text("Eliminar")}
        },
        dismissButton = {
            Button(onClick = onDissmiss) { Text("Cancelar")}
        }
    )
}