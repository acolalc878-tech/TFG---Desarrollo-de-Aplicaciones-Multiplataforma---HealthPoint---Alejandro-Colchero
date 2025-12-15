package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.GestionCitasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoSolicitudesPacienteScreen(
    idPaciente: String,
    onBack: () -> Unit,
    viewModel: GestionCitasViewModel = viewModel(),
) {
    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(idPaciente) {
        viewModel.cargarCitasUsuario(idPaciente)
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {Text("Estado de mis solicitudes", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
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
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            if(loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            } else if (citas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No tienes solcitudes de citas registradas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    "Mostrando ${citas.size} solicitudes:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(citas) { cita ->
                        SolicitudPacienteCard(cita = cita)
                    }
                }
            }
        }
    }
}

// Card que muestra el resultado de una solicitud para el paciente
@Composable
fun SolicitudPacienteCard(cita: Cita) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val errorColor = MaterialTheme.colorScheme.error

    val (estadoTexto, estadoColor, containerColor) = when (cita.estado.uppercase()) {
        "PENDIENTE" -> Triple("Pendiente de Revisión", Color(0xFFFFA726), Color(0xFFFFA726).copy(alpha = 0.1f))
        "ACEPTADA" -> Triple("Solicitud Aprobada", Color(0xFF4CAF50), Color(0xFF4CAF50).copy(alpha = 0.1f))
        "RECHAZADA", "FINALIZADA" -> Triple("Solicitud Rechazada", errorColor, errorColor.copy(alpha = 0.1f))
        else -> Triple(cita.estado, onSurfaceColor, onSurfaceColor.copy(alpha = 0.05f))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = estadoTexto,
                    color = estadoColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            color = containerColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Divider(color = primaryColor.copy(alpha = 0.1f))
            Spacer(Modifier.height(16.dp))

            Text(
                "Motivo de la solicitud:",
                style = MaterialTheme.typography.labelMedium,
                color = onSurfaceColor.copy(alpha = 0.6f)
            )
            Text(
                cita.motivo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(10.dp))

            if (cita.fecha.isNotBlank() && cita.hora.isNotBlank()) {
                Text(
                    "Fecha y hora asignadas:",
                    style = MaterialTheme.typography.labelMedium,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )
                Text(
                    "${cita.fecha} a las ${cita.hora}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Text(
                    "Detalles de fecha y hora:",
                    style = MaterialTheme.typography.labelMedium,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )
                Text(
                    "Aún no asignados",
                    style = MaterialTheme.typography.bodyLarge,
                    color = onSurfaceColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            if (cita.Id_centroMedico.isNotBlank()) {
                Text(
                    "Centro Médico:",
                    style = MaterialTheme.typography.labelMedium,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )
                Text(
                    cita.Id_centroMedico,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (cita.notasMedico.isNotBlank() && cita.estado.uppercase() != "PENDIENTE") {
                Spacer(Modifier.height(16.dp))
                Divider(color = primaryColor.copy(alpha = 0.1f))
                Spacer(Modifier.height(16.dp))

                Text(
                    "Nota del Médico:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor
                )
                Text(
                    cita.notasMedico,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceColor.copy(alpha = 0.75f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}