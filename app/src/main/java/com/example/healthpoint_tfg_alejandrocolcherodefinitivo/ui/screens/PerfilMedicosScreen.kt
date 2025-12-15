package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.CentroMedicoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedicoScreen(
    idMedico: String,
    onBack:() -> Unit,
    viewModel: CentroMedicoViewModel = viewModel()
) {

    LaunchedEffect(idMedico) {
        viewModel.cargarMedico(idMedico)
    }

    // Estados
    val medico by viewModel.medico.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val actualizacionExitosa by viewModel.actualizacionExitosa.collectAsState()
    val centrosDisponibles by viewModel.centrosDisponibles.collectAsState()

    // Snack bar para los mensajes de cambios
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Campos a editar
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var numColegiado by remember { mutableStateOf("") }

    // Control del desplegable de los centros medicos
    var idCentroSeleccionado by remember { mutableStateOf("") }
    var centroAbierto by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary

    // Efectosy sincronizacion
    LaunchedEffect(idMedico) {
        // Cargamos el perfil del medico con el que hemos hecho el login
        viewModel.cargarMedico(idMedico)
        viewModel.cargarCentrosDisponibles() // Con esto cargamos los centros medicos de la base de datos
    }

    LaunchedEffect(medico) {
        medico?.let {
            nombre = it.nombre
            apellidos = it.apellidos
            especialidad = it.especialidad
            horario = it.horario
            numColegiado = it.numColegiado
            idCentroSeleccionado = it.Id_CentroMedico
        }
    }

    LaunchedEffect(actualizacionExitosa) {
        if(actualizacionExitosa) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Perfil actualizado con exito: ${medico?.Id_CentroMedico}",
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.noSnackBackRepeat()
        }
    }

    // Mostramos el nombre de los centros en el desplegable
    val centroActual = centrosDisponibles.find { it.Id_centroMedico == idCentroSeleccionado }
    val centroDisplay = centroActual?.nombreCentro ?: "Seleccione un centro médico:"

    // Se verifica que se hayan hecho cambios para hacer clickable el boton de guardar
    val hasChanged = medico?.let {
        it.nombre != nombre ||
                it.apellidos != apellidos ||
                it.especialidad != especialidad ||
                it.horario != horario ||
                it.numColegiado != numColegiado ||
                it.Id_CentroMedico != idCentroSeleccionado
    } ?: false

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = { Text("Perfil y centro del médico", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Informacion de los campos y edicion de campos
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = especialidad,
                onValueChange = { especialidad = it },
                label = { Text("Especialidad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numColegiado,
                onValueChange = { numColegiado = it },
                label = { Text("Nº de Colegiado") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = horario,
                onValueChange = { horario = it },
                label = { Text("Horario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Desplegable de los centros médicos
            Text("Centro medico asignado: ", style = MaterialTheme.typography.labelLarge)
            Box(Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = centroAbierto,
                    onExpandedChange = { centroAbierto = !centroAbierto }
                ) {
                    OutlinedTextField(
                        value = centroDisplay,
                        onValueChange = {/*Solo lectura*/ },
                        readOnly = true,
                        label = { Text("Selecciona Centro") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = centroAbierto) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = centroAbierto,
                        onDismissRequest = { centroAbierto = false }
                    ) {
                        centrosDisponibles.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c.nombreCentro) },
                            onClick = {
                                idCentroSeleccionado = c.Id_centroMedico
                                centroAbierto = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

            // Botones de accion
            Button(
                onClick = {
                    if(medico != null){
                        val medicoActualizado = medico!!.copy(
                        nombre = nombre.trim(),
                        apellidos = apellidos.trim(),
                        especialidad = especialidad.trim(),
                        horario = horario.trim(),
                        numColegiado = numColegiado.trim(),
                        Id_CentroMedico = idCentroSeleccionado
                        )
                        viewModel.actualizarPerfilMedico(medicoActualizado)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && hasChanged
            ) {
                Text(if (loading) "Guardando..." else "Guardar cambios del perfil")
            }
            Spacer(Modifier.height(24.dp))

            // Indicadores
            if(loading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if(!error.isNullOrEmpty()){
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}