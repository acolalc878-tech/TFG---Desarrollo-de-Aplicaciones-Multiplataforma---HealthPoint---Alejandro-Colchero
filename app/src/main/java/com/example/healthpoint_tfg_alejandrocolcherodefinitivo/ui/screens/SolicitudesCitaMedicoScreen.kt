package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.SolicitudCita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.SolicitarCitasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudesCitaMedicoScreen(
    idMedico: String,
    viewModel: SolicitarCitasViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Observar el estado de las solicitudes y la carga
    val solicitudes by viewModel.solicitudes.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    // Cargar las solicitudes solo una vez al inicio o cuando el ID cambie
    LaunchedEffect(idMedico) {
        if(idMedico.isNotBlank())
            viewModel.cargarSolicitudes(idMedico)
    }


    Scaffold(
        containerColor = Color(0xFFF7F9FC), // Fondo blanco suave
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes de Citas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onPrimaryColor)},
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

        // 1. Mostrar indicador de carga si los datos están pendientes
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // 2. Mostrar mensaje de vacío si no hay solicitudes y la carga ha terminado
        if (solicitudes.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes solicitudes pendientes")
            }
        } else {
            // 3. Mostrar la lista de solicitudes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(solicitudes, key = { it.id_solicitud }) { solicitud ->
                    SolicitudCitaCard(
                        solicitud = solicitud,
                        onAceptar = { viewModel.aceptarSolicitud(solicitud) },
                        onRechazar = { viewModel.rechazarSolicitud(solicitud) }
                    )
                }
            }
        }
    }
}

@Composable
fun SolicitudCitaCard(
    solicitud: SolicitudCita,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text("Paciente: ${solicitud.id_usuario}", style = MaterialTheme.typography.titleMedium)
            Text("Especialidad: ${solicitud.especialidad}", style = MaterialTheme.typography.bodyMedium)
            Text("Motivo: ${solicitud.motivo}", style = MaterialTheme.typography.bodyMedium)
            Text("Estado: ${solicitud.estado}", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onAceptar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Aceptar")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = onRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Rechazar")
                }
            }
        }
    }
}