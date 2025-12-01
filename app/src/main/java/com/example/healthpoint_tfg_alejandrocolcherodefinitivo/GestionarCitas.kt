package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarCitas(
    idMedico: String,
    onBack:() -> Unit,
    viewModel: GestionCitasViewModel = viewModel()
) {

    LaunchedEffect(idMedico) {
        println("Se estan cargando las citas de medico con id: $idMedico")
        viewModel.cargarCitasMedico(idMedico)
    }

    val citas = viewModel.citas.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val mensaje = viewModel.mensaje.collectAsState().value


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestion de citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(onClick = {
                viewModel.crearCita(
                    Cita(
                        Id_usuario = "user001",
                        Id_medico = idMedico,
                        Id_centroMedico = "centro01",
                        fecha = "10/12/2025",
                        hora = "10:00",
                        motivo = "Nueva Cita",
                        estado = "Pendiente",
                        notasMedico = ""
                    )
                )
            }) {
                Text("Crear nueva cita")
            }

            Spacer(Modifier.height(20.dp))

            when {
                loading -> Text("Cargando...")
                citas.isEmpty() -> Text("No hay citas registradas")
                else -> {
                    citas.forEach { cita ->
                        CitasCard(
                            cita = cita,
                            onDelete = {
                                viewModel.eliminarCita(cita.Id_cita)
                            },
                            onChangeEstado = { nuevoEstado ->
                                viewModel.cambiarEstado(cita.Id_cita, nuevoEstado)
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CitasCard(
    cita: Cita,
    onDelete: () -> Unit,
    onChangeEstado: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.bodyLarge)
            Text("Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
            Text("Motivo: ${cita.motivo}")
            Text("Estado: ${cita.estado}")

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {

                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        val nuevoEstado =
                            if (cita.estado == "Pendiente") "Completada"
                            else "Pendiente"

                        onChangeEstado(nuevoEstado)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cambiar estado")
                }
            }
        }
    }
}
