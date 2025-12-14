package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.CitasPacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasPacienteScreen(
    idPaciente: String,
    viewModel: CitasPacienteViewModel = viewModel(),
    onBack: () -> Unit
) {
    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargamos las citas al entrar en la pantalla
    LaunchedEffect(idPaciente) {
        viewModel.cargarCitasPorUsuario(idPaciente)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Crossfade(
                targetState = Triple(loading, citas, error),
                label = "citasCrossfade"
            ) { (isLoading, listaCitas, err) ->

                when {
                    isLoading -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                    err != null -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(err ?: "") }

                    listaCitas.isEmpty() -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text("No tienes citas") }

                    else -> LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaCitas) { citaDisplay -> CitaCardPaciente(citaDisplay) }
                    }
                }
            }
        }
    }
}

@Composable
fun CitaCardPaciente(citaDisplay: CitaDisplay){
    val cita = citaDisplay.cita
    val nombreMedico = citaDisplay.nombreMedico

    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    // Funcion para obtener el color del estado
    val getStatusColor: @Composable (String) -> Color = { estado ->
        when (estado.lowercase()) {
            "pendiente" -> Color(0xFFFFA726)
            "confirmada" -> Color(0xFF4CAF50)
            "cancelada" -> MaterialTheme.colorScheme.error
            else -> onSurfaceColor.copy(alpha = 0.6f)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Fecha y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${cita.fecha} ${cita.hora}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceColor
                )

                Text(
                    text = cita.estado.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = getStatusColor(cita.estado),
                    modifier = Modifier
                        .background(
                            color = getStatusColor(cita.estado).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Divider(color = primaryColor.copy(alpha = 0.1f))
            Spacer(Modifier.height(16.dp))

            // Detalles (Nombre del medico)
            Text("Médico: $nombreMedico", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(10.dp))

            Text("Motivo: ${cita.motivo}", style = MaterialTheme.typography.bodyLarge)

            // Notas del medico (si las hay)
            if(cita.notasMedico.isNotBlank()){
                Spacer(Modifier.height(16.dp))
                Divider(color = primaryColor.copy(alpha = 0.1f))
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Notas del médico:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = onSurfaceColor.copy(alpha = 0.8f)
                )

                Text(
                    text = cita.notasMedico,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}