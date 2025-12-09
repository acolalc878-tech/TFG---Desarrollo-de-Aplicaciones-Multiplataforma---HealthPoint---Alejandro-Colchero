package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.MedicamentosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarMedicamentosScreen(
    onBack: () -> Unit,
    viewModel: MedicamentosViewModel = viewModel()
) {
    val query by viewModel.sentencia.collectAsState()
    val resultados by viewModel.resultados.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val detalle by viewModel.medicamentoDetalle.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Medicamentos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            if(detalle == null){
                // Buscador
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        viewModel.actualizarQuery(it)
                        viewModel.buscarSugerencia() },
                    placeholder = { Text("Buscar por nombre (mínimo 3 letras)...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                if (loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(16.dp))

                when {
                    error != null -> {
                        Text("Error de conexion $error", color = MaterialTheme.colorScheme.error)
                    }

                    !loading && resultados.isEmpty() && query.length >= 3 -> {
                        Text("No se encontraron coincidencias para \"$query\".", style = MaterialTheme.typography.bodyMedium)
                    }

                    !loading && query.length < 3 -> {
                        Text("Introduce al menos 3 caracteres para buscar.", style = MaterialTheme.typography.bodySmall)
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ){
                            items(resultados) { sugerencia ->
                                CardSugerencia(
                                    name = sugerencia,
                                    onClick = {
                                        viewModel.buscarSugerencia()
                                    }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardSugerencia(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(
                "Haz click para ver la información más detallada",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
