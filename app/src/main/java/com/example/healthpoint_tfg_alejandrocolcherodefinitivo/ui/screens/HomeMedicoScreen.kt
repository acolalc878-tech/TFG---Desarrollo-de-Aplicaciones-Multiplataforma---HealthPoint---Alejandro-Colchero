package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.MedicoViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMedicoScreen(
    idUsuario: String,
    viewModel: MedicoViewModel = viewModel(),
    onLogout: () -> Unit,
    onGestionarPacientes: (String) -> Unit,
    onGestionarCitas: (idMedico: String) -> Unit,
    onGestionarTratamientos: (String) -> Unit,
    onVerPerfil: (String) -> Unit,
    onBuscarMedicamentos: (String) -> Unit,
    onGestionarSolicitudes: (idMedico: String) -> Unit,
) {

    // Observables
    val medicoState by viewModel.medico.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val idMedico = medicoState?.Id_medico ?: ""

    // Cargar datos del medico al entrar (resuelve Id_medico)
    LaunchedEffect(idUsuario) {
        viewModel.cargarMedicoPorUsuario(idUsuario)
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "HealthPoint - Médico",
                        color = surfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                ),
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = surfaceColor
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                val nombreVisible = medicoState?.nombre ?: "Medico"
                val apellidosVisible = medicoState?.apellidos ?: ""

                Text(
                    "$nombreVisible $apellidosVisible",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Menú de Acciones Rápidas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tarjetas de accion de cuadricula
            val actions = listOf(
                ActionItem(
                    label = "Gestionar Pacientes",
                    icon = Icons.Default.Person,
                    onClick = { onGestionarPacientes(idMedico) }
                ),

                ActionItem(
                    label = "Gestionar Citas",
                    icon = Icons.Default.DateRange,
                    onClick = { onGestionarCitas(idUsuario) }
                ),

                ActionItem(
                    label = "Gestionar Tratamientos",
                    icon = Icons.Default.Favorite,
                    onClick = { onGestionarTratamientos(idMedico) }
                ),

                ActionItem(
                    label = "Buscar Medicamentos",
                    icon = Icons.Default.Search,
                    onClick = { onBuscarMedicamentos(idMedico) }
                ),

                ActionItem(
                    label = "Ver perfil y centro médico",
                    icon = Icons.Default.Person,
                    onClick = { onVerPerfil(idMedico) }
                ),

                ActionItem(
                    label = "Solicitudes de Cita",
                    icon = Icons.Default.Info,
                    onClick = { onGestionarSolicitudes(idMedico) }
                ),

            )
            items(actions) { item ->
                ActionCard(item)
            }
            item {
                error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Error de carga: $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ActionCard(item: ActionItem){
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = item.onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    item.icon,
                    contentDescription = item.label,
                    tint = primaryColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    item.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = primaryColor.copy(alpha = 0.7f)
            )
        }
    }
}

data class ActionItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)