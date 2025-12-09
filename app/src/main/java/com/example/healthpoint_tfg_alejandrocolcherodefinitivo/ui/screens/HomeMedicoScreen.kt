package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onVerCentro: (String) -> Unit,
    onVerPerfil: (String) -> Unit,
    onBuscarMedicamentos: (String) -> Unit
) {

    // Observables
    val medicoState by viewModel.medico.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargar datos del medico al entrar (resuelve Id_medico)
    LaunchedEffect(idUsuario) {
        viewModel.cargarMedicoPorUsuario(idUsuario)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HealthPoint - Médico") },
                actions = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.onPrimary)
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
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val nombreVisible = medicoState?.nombre ?: "Doctor/a"


            Text(
                "Bienvenid@, $nombreVisible",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Seleccione qué desea gestionar:", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(32.dp))

            val idMedico = medicoState?.Id_medico ?: ""

            // Botones de acciones
            Button(
                onClick = { onGestionarPacientes(idMedico) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Pacientes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onGestionarCitas(idUsuario) }, modifier = Modifier.fillMaxWidth()) {
                Text("Gestionar Citas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onGestionarTratamientos(idMedico) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Tratamientos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onVerCentro(medicoState?.Id_CentroMedico ?: "") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Información del centro médico asignado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onBuscarMedicamentos(idMedico) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Medicamentos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onVerPerfil(idMedico) }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Perfil")
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Error")
            }
        }
    }
}