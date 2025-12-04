package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePacienteScreen(
    onLogout: () -> Unit,
    onVerCita: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerTratamientos: () -> Unit,
    onVerPerfil: () -> Unit
) {

    // Estado de la pestaña activa
    var selectedIndex by remember { mutableStateOf(0)}

    val items = listOf(
        BottomItem("Inicio", Icons.Default.Home),
        BottomItem("Citas", Icons.Default.DateRange),
        BottomItem("Tratamientos", Icons.Default.Favorite),
        BottomItem("Perfil", Icons.Default.Person)
    )

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

        bottomBar = {
            NavigationBar {
                items.forEachIndexed{index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index

                            when(index) {
                                1 -> onVerCita()
                                2 -> onVerTratamientos()
                                3 -> onVerPerfil()
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label)},
                        label = { Text(item.label)}
                    )
                }
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
        ) {

            // Titulo para citas
            Text(
                text = "Tus proximas citas...",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            val ejemploCitas = List(3) { index ->
                CitaResumen(
                    fecha = "22/10/2026",
                    hora = "10:20 AM",
                    medico = "Dr.Alejandro Colchero",
                    estado = "Confirmada"
                )
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(ejemploCitas) { cita ->
                    detallesCitaCard(cita)
                }
            }
        }
    }
}

data class BottomItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class CitaResumen(
    val fecha: String,
    val hora: String,
    val medico: String,
    val estado: String
)

@Composable
fun detallesCitaCard(cita: CitaResumen) {
    Card(
        modifier = Modifier.width(200.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${cita.fecha}", style = MaterialTheme.typography.titleMedium)
            Text("${cita.hora}")
            Text("⚕${cita.medico}")
            Text("Estado: ${cita.estado}", color = MaterialTheme.colorScheme.primary)
        }
    }
}