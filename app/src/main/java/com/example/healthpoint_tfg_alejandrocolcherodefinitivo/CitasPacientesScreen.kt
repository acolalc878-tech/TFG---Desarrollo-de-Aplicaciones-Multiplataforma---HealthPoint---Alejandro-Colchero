package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasPacienteScreen(
    viewModel: CitasPacienteViewModel,
    onBack: () -> Unit
) {
    val citas by viewModel.citas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargamos las citas al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarCitaPacientes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Crossfade(
                targetState = Triple(loading, citas, error),
                label = "citasCrossfade"
            ) { (isLoading, listaCitas, errorMsg) ->

                when {
                    isLoading -> LoadingView()
                    errorMsg != null -> ErrorView(errorMsg)
                    listaCitas.isEmpty() -> EmptyView()
                    else -> CitasList(lista = listaCitas)
                }
            }
        }
    }
}

@Composable
fun LoadingView(){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text("Cargando tus citas...")
    }
}

@Composable
fun EmptyView() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No tienes citas registradas", color = Color.Gray)
    }
}

@Composable
fun ErrorView(msg: String) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ERROR al cargar las citas")
        Spacer(Modifier.height(12.dp))
        Text(msg, color = Color.Red)
    }
}

@Composable
fun CitasList(lista: List<Cita>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(lista) { cita ->
            CitaCard(cita)
        }
    }
}

@Composable
fun CitaCard(cita: Cita){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Medico: ${cita.id_medico}")
            Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.titleMedium)
            Text("Hora: ${cita.hora}")
            Text("Estado: ${cita.estado}")
            Text("Motivo: ${cita.motivo}")
            Text("Notas doctor: ${cita.notasMedico}")

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Notas del médico: ",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = cita.notasMedico.ifEmpty { "Sin notas" },
                color = Color.DarkGray
            )
        }
    }
}