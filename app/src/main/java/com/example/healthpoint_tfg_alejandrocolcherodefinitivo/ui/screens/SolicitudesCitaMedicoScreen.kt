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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val solicitudes by viewModel.solicitudes.collectAsState()

    LaunchedEffect(idMedico) {
        if(idMedico.isNotBlank())
        viewModel.cargarSolicitudes(idMedico)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes de Citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        if (solicitudes.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes solicitudes pendientes")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(solicitudes) { solicitud ->
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

            Text("Paciente: ${solicitud.Id_usuario}")
            Text("Especialidad: ${solicitud.especialidad}")
            Text("Motivo: ${solicitud.motivo}")
            Text("Estado: ${solicitud.estado}")

            Spacer(Modifier.height(12.dp))

            Row {
                Button(
                    onClick = onAceptar,
                    modifier = Modifier.weight(1f)
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

