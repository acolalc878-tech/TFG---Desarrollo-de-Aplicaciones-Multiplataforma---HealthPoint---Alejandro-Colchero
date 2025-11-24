package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMedicoScreen(
    onLogout: () -> Unit,
    onGestionarPacientes: () -> Unit,
    onGestionarCitas: () -> Unit,
    onGestionarTratamientos: () -> Unit,
    onVerCentro: () -> Unit,
    onVerPerfil: () -> Unit,
    onBuscarMedicamentos: () -> Unit
) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Bienvenid@, Doctor/a",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Seleccione qué desea gestionar:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acciones

            // Botón 1
            Button(
                onClick = onGestionarPacientes,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Pacientes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón 2
            Button(
                onClick = onGestionarCitas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Citas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGestionarTratamientos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Tratamientos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVerCentro,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Información del centro médico asignado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBuscarMedicamentos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Medicamentos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVerPerfil,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Perfil")
            }
        }
    }
}