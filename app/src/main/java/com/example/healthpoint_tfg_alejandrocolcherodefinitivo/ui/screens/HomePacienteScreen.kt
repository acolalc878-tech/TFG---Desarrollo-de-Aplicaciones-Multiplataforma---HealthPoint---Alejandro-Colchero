package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.CitasPacienteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePacienteScreen(
    idUsuario: String,
    onLogout: () -> Unit,
    onVerCita: () -> Unit,
    onVerTratamientos: () -> Unit,
    onVerPerfil: () -> Unit,
    onSolicitarCita: (String) -> Unit,
    onVerEstadoSoliciatudes: () -> Unit
) {

    val viewModel: CitasPacienteViewModel = viewModel()
    val citas by viewModel.citas.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.error

    LaunchedEffect(idUsuario) {
        viewModel.cargarCitasPorUsuario(idUsuario)
    }

    var selectedIndex by remember { mutableStateOf(0) }

    val bottomItems = listOf(
        BottomItem("Inicio", Icons.Default.Home),
        BottomItem("Citas", Icons.Default.DateRange),
        BottomItem("Tratamientos", Icons.Default.Favorite),
        BottomItem("Solicitudes", Icons.Default.List),
        BottomItem("Perfil", Icons.Default.Person)
    )

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "HealthPoint - Paciente",
                        color = surfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                        },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                ),
                actions = {
                    TextButton(onClick = onLogout) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = surfaceColor
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = surfaceColor,
                tonalElevation = 4.dp
            ) {
                bottomItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            when (index) {
                                1 -> onVerCita()
                                2 -> onVerTratamientos()
                                3 -> onVerEstadoSoliciatudes()
                                4 -> onVerPerfil()
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            indicatorColor = primaryColor.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            ) {

            Button(
                onClick = { onSolicitarCita(idUsuario) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Solicitar Cita",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Solicitar Cita",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Titulo para citas
            Text(
                text = "Tus próximas citas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (citas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center){
                        Text("Parece que no tienes citas pendientes. ¡Disfruta!", color = onSurfaceColor.copy(alpha = 0.7f))
                    }
                }
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(citas) { citaDisplay ->

                        val cita = citaDisplay.cita
                        val nombreMedico = citaDisplay.nombreMedico

                        detallesCitaCard(
                            fecha = cita.fecha,
                            hora = cita.hora,
                            medico = nombreMedico,
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

    val primaryColor = MaterialTheme.colorScheme.primary
    val statusColor = when (estado.lowercase()){
        "pendiente" -> Color(0xFFFFA726)
        "confirmada" -> Color(0xFF66BB6A)
        "cancelada" -> MaterialTheme.colorScheme.error
        else -> primaryColor.copy(alpha = 0.8f)
    }

    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Cabecera con la fecha e icono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = primaryColor
                )
            }

            Spacer(Modifier.height(8.dp))
            Divider(color = primaryColor.copy(alpha = 0.3f))
            Spacer(Modifier.height(8.dp))

            //Hora y medico
            Text("Hora: $hora", style = MaterialTheme.typography.bodyMedium)
            Text("Médico: $medico", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))

            // Estado
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor)
                )
                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Estado: $estado",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }

        }
    }
}

data class BottomItem(
    val label: String,
    val icon: ImageVector
)

data class CitaDisplay(
    val cita: Cita,
    val nombreMedico: String
)