package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Paciente
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.GestionarPacientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarPacientesScreen(
    idPaciente: String,
    onBack: () -> Unit,
    viewModel: GestionarPacientesViewModel = viewModel()

) {
    val pacientes = viewModel.pacientes
    val busqueda = viewModel.busqueda

    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(idPaciente) {
        viewModel.buscarPacientes(idPaciente)
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Pacientes", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                label = { Text("Buscar paciente") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            pacientes.forEach { paciente ->
                PacienteCard(paciente)
                Spacer(Modifier.height(8.dp))
            }

            if (pacientes.isEmpty() && busqueda.isNotEmpty()) {
                Text("No se encontraron pacientes.")
            }
        }
    }
}

@Composable
fun PacienteCard(p: Paciente) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("${p.nombre} ${p.apellido}", style = MaterialTheme.typography.titleMedium)
            Text("Email: ${p.email}")
            Text("Teléfono: ${p.telefono}")
            Spacer(Modifier.height(8.dp))
            Text("ID usuario: ${p.Id_paciente}", color = MaterialTheme.colorScheme.primary)
        }
    }
}