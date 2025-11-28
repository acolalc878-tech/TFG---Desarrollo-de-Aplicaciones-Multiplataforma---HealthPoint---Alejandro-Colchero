package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionCitasScreen(
    idMedico: String,
    onBack: () -> Unit,
    viewModel: GestionCitasViewModel = viewModel()
) {
    LaunchedEffect(idMedico) {
        viewModel.cargarCitasMedico(idMedico)
    }

    val citas = viewModel.citas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Button(
             onClick = {
                 viewModel.crearCita(
                     Cita(
                         Id_usuario = "Sin asignar",
                         Id_medico = idMedico,
                         Id_centroMedico = "centro001",
                         fecha = "21/10/2026",
                         hora = "10:00",
                         motivo = "Revision Urgente de Sangre",
                         estado = "PENDIENTE",
                         notasMedico = ""
                     )
                 )
             }
            ) {
                Text("Crear nueva cita")
            }

            Spacer(Modifier.height(16.dp))

            citas.forEach { cita ->
                CitaCard(
                    cita = cita,
                    onDelete = { viewModel.eliminarCita(cita.Id_Cita) },
                    onChangeEstado = {
                        viewModel.actualizarCita(cita.Id_Cita, mapOf("estado" to it))
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CitaCard(
    cita: Cita,
    onDelete: () -> Unit,
    onChangeEstado: (String) -> Unit
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Fecha: ${cita.fecha}")
            Text("Hora: ${cita.hora}")
            Text("Paciente: ${cita.Id_usuario}")
            Text("Motivo: ${cita.motivo}")
            Text("Estado actual: ${cita.estado}")

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onChangeEstado("Pendiente") }) { Text("Pendiente") }
                Button(onClick = { onChangeEstado("Completada") }) { Text("Completada") }
                Button(onClick = { onChangeEstado("Cancelada") }) { Text("Cancelada") }
            }

            Spacer(Modifier.height(8.dp))

            Button(onClick = onDelete) {
                Text("Eliminar cita")
            }
        }
    }
}