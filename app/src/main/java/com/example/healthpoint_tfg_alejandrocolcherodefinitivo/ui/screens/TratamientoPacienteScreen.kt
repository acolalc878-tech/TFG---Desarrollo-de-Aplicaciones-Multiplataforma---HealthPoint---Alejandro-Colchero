package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    LaunchedEffect(idPaciente) {
        viewModel.cargarTratamientosPorPaciente(idPaciente)
    }

    val tratamientos by viewModel.tratamientos.collectAsState()
    val isLoading by viewModel.loading
    val errorMsg by viewModel.error

    val primaryColor = MaterialTheme.colorScheme.primary

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = { Text("Mis Tratamientos", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ){ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = if (isLoading || tratamientos.isEmpty() || errorMsg != null) Alignment.Center else Alignment.TopCenter
        ) {
            when {
                isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = primaryColor)
                        Spacer(Modifier.height(8.dp))
                        Text("Cargando tratamientos...", color = primaryColor)
                    }
                }
                errorMsg != null -> {
                    Text("Error de carga: $errorMsg", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                }
                tratamientos.isEmpty() -> {
                    Text("No tienes tratamientos activos asignados.", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = padding,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tratamiento: ${tratamiento.descripcion}",
                style= MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Indicaciones:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = tratamiento.indicaciones,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Duración: ${tratamiento.duracionDias} días",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}