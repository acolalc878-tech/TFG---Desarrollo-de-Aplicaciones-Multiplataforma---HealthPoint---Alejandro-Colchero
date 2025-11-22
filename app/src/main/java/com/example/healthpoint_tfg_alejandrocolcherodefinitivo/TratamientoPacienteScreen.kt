package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TratamientoPacienteScreen(
    viewModel: TratamientosPacienteViewModel = viewModel(),
    onBack: () -> Unit
) {
    val tratamientos by viewModel.tratamientos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {viewModel.cargarTratamientos()}

    Scaffold(topBar = {
        TopAppBar(title = {Text("Mis Tratamientos")}, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
        })
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Crossfade(targetState = Triple(loading, tratamientos, error)) { (isLoading, lista, err) ->
                when {
                    isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator()}
                    err !=  null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(err ?: "")}
                    lista.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No tienes tratamientos")}
                    else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(lista) { t -> TratamientoCard(t)}
                    }
                }
            }
        }
    }
}

@Composable
fun TratamientoCard(t: Tratamiento) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(t.descripcion.ifEmpty { t.Id_tratamiento }, style = MaterialTheme.typography.titleMedium)
            Text("Duracion: ${t.duracionDias} días")
            Text("Indicaciones: ${t.indicaciones}")
        }
    }
}