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
fun HomeMedicoScreen(onLogout: () -> Unit, onVerCita: () -> Unit) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenid@, Doctor/a",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Aquí podrá consultar todas sus citas, pacientes asignados y añadir anotaciones.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón 1
            Button( onClick = onVerCita,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver mis citas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón 2
            Button(
                onClick = { /* navegar al historial */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Historial Médico")
            }
        }
    }
}