package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.TratamientosPacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TratamientoPacienteScreen(
    idPaciente: String,
    viewModel: TratamientosPacienteViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Cargarmos los tratamientosal entrar en la pantalla
    LaunchedEffect(idPaciente) {viewModel.cargarTratamientosPorPaciente(idPaciente)}

    // Observamos el estado del ViewModel
    val tratamientos by viewModel.tratamientos.collectAsState()
    val isLoading by viewModel.loading
    val errorMsg by viewModel.error

    Scaffold(
        topBar = { TopAppBar(title = {Text("Mis Tratamientos")})}
    ){ padding ->
        Column(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(padding)
                 .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                    Text("Cargando tratamientos...")
                }
                errorMsg != null -> {
                    Text("Error: $errorMsg", color = MaterialTheme.colorScheme.error)
                }
                tratamientos.isEmpty() -> {
                    Text("No tienes tratamientos activos asignados.", style = MaterialTheme.typography.titleMedium)
                }

                else -> {
                    // Mostramos la lista de los tratamientos
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tratamientos) { tratamiento ->
                            TratamientoCard(tratamiento = tratamiento)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TratamientoCard(tratamiento: Tratamiento) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tratamiento: ${tratamiento.descripcion}",
                style= MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Indicaciones: ${tratamiento.indicaciones}")
            Text(text = "Duracion: ${tratamiento.duracionDias} días")
            // Mostrar a qué cita está vinculada
            Text(
                text = "Asignado a cita con ID: ${tratamiento.Id_cita.take(8)}...",
                style = MaterialTheme.typography.bodySmall
                )
        }
    }
}