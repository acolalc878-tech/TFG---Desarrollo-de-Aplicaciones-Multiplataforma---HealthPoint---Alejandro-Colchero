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
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.CitasPacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasPacienteScreen(
    idPaciente: String, // id del paciente
    viewModel: CitasPacienteViewModel = viewModel(), // ViewModel implementado
    onBack: () -> Unit
) {
    // Recoleccion de estados desde el ViewModel
    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary

    // Cargamos las citas al entrar en la pantalla
    LaunchedEffect(idPaciente) {
        viewModel.cargarCitasPorUsuario(idPaciente)
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->

        // Contenedor Principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Transicion de Estados
            // Aplicamos una animacion suave cuando la interfaz cambia entre los diferentes estados (carga, error, resultados, vacío).
            Crossfade(
                targetState = Triple(loading, citas, error),
                label = "citasCrossfade"
            ) { (isLoading, listaCitas, err) ->

                // Manejo condicional de Estados de la interfaz
                when {
                    isLoading -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() } // Muestra indicador de carga.

                    err != null -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(err ?: "") } // Muestra mensaje de error

                    listaCitas.isEmpty() -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text("No tienes citas") } // Muestra mensaje de lista vacia

                    else -> LazyColumn(
                        // Muestrala lista de citas usando LazyColumn para optimizar el rendimiento
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

    // Definicion de colores y estilos
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
        shape = RoundedCornerShape(12.dp), // redondeamos el borde de la carta
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Sección 1: Fecha, Hora y Estado Dinámico
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Fecha y Hora de la cita
                Text(
                    "${cita.fecha} ${cita.hora}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceColor
                )

                // Etiqueta del estado (con color de fondoy texto dinámico)
                Text(
                    text = cita.estado.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = getStatusColor(cita.estado),
                    // Aplicamos fondo con baja opacidad del color del estadopara contraste
                    modifier = Modifier
                        .background(
                            color = getStatusColor(cita.estado).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Separadores
            Spacer(Modifier.height(16.dp))
            Divider(color = primaryColor.copy(alpha = 0.1f))
            Spacer(Modifier.height(16.dp))

            // Detalles (Nombre del medico)
            Text("Médico: $nombreMedico", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(10.dp))

            Text("Motivo: ${cita.motivo}", style = MaterialTheme.typography.bodyLarge)

            // Sección 2: Notas del Médico (Condicional)
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