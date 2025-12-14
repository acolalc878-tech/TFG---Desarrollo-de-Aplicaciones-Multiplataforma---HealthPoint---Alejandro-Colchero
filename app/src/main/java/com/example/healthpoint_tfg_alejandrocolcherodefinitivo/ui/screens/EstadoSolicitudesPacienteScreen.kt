package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
    viewModel: GestionCitasViewModel = viewModel()
) {
    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(idPaciente) {
        viewModel.cargarCitasUsuario(idPaciente)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Estado de mis solicitudes")},
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
    val (estadoTexto, estadoColor) = when (cita.estado.uppercase()) {
        "PENDIENTE" -> Pair("Pendiente de Revision", Color(0xFFFFA726))
        "ACEPTADA" -> Pair("Solicitud Aprobada", Color(0xFF4CAF50))
        "FINALIZADA" -> Pair("Solicitud Rechazada", MaterialTheme.colorScheme.error)
        else -> Pair(cita.estado, MaterialTheme.colorScheme.onSurface)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Estado
            Text(
                text = estadoTexto,
                color = estadoColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Divider(Modifier.padding(vertical = 8.dp))

            // Detalles de la solicitud
            Text("Motivo de la solicitud", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(cita.motivo, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

            Spacer(Modifier.height(8.dp))

            if(cita.fecha.isNotBlank() && cita.hora.isNotBlank()){
                Text("Fecha y hora: ${cita.fecha} a las ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("Detalles de fecha y hora: Aún no asignados", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            if(cita.Id_centroMedico.isNotBlank()){
                Text("Centro medico: ${cita.Id_centroMedico}", style = MaterialTheme.typography.bodyLarge)
            }

            if(cita.notasMedico.isNotBlank() && cita.estado.uppercase() != "PENDIENTE") {
                Spacer(Modifier.height(8.dp))
                Text("Nota del Médico:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                Text(cita.notasMedico, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}