package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePacienteScreen(
    idPaciente: String,
    onLogout: () -> Unit,
    onVerCita: () -> Unit,
    onVerHistorial: () -> Unit,
    onVerTratamientos: () -> Unit,
    onVerPerfil: () -> Unit
) {

    val viewModel:  CitasPacienteViewModel = viewModel()
    val citas by viewModel.citas.collectAsState()

    LaunchedEffect(idPaciente) {
        viewModel.cargarCitasPorUsuario(idPaciente)
    }

    var selectedIndex by remember { mutableStateOf(0) }

    val bottomItems = listOf(
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
                bottomItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            when (index) {
                                1 -> onVerCita()
                                2 -> onVerTratamientos()
                                3 -> onVerPerfil()
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
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
                text = "Tus próximas citas",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (citas.isEmpty()) {
                Text("No tienes citas asignadas.")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(citas) { cita ->
                        detallesCitaCard(
                            fecha = cita.fecha,
                            hora = cita.hora,
                            medico = cita.Id_medico,
                            estado = cita.estado
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun detallesCitaCard(fecha: String, hora: String, medico: String, estado: String) {
    Card(
        modifier = Modifier.width(200.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(fecha, style = MaterialTheme.typography.titleMedium)
            Text(hora)
            Text("Médico: $medico")
            Text("Estado: $estado")
        }
    }
}

data class BottomItem(
    val label: String,
    val icon: ImageVector
)