package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePacienteScreen(
    onLogout: () -> Unit,
    onVerCita: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerTratamientos: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HealthPoint - Paciente") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenid@ Paciente",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Aquí puedes gestionar tus citas, historial y tratamientos",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onVerCita,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Mis citas - HealthPoint")
                }

                Button(
                    onClick = onVerHistorial,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Historial médico del/la paciente")
                }

                Button(
                 onClick = onVerTratamientos,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Tratamientos")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tartejtas de resumen (ejemplo de ultimas citas)
            Text("últomas citas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            val ejemploCitas = List(3) {
                index ->
                CitaResumen(
                    fecha = "22/10/2026",
                    hora = "10:20 AM",
                    medico = "Dr.Alejandro Colchero",
                    estado = "Confirmada"
                )
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(ejemploCitas) {cita ->
                    detallesCitaCard(cita)
                }
            }
        }
    }
}

data class CitaResumen(val fecha: String, val hora: String, val medico: String, val estado: String)

@Composable
fun detallesCitaCard(cita: CitaResumen) {
    Card(
        modifier = Modifier.width(180.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.bodyMedium)
            Text("Hora: ${cita.hora}")
            Text("Médico: ${cita.medico}")
            Text("Estado: ${cita.estado}")
        }
    }
}

@Composable
fun navegacionAbajo() {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, contentDescription = null) })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.DateRange, contentDescription = null) })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Person, contentDescription = null) })
    }
}
