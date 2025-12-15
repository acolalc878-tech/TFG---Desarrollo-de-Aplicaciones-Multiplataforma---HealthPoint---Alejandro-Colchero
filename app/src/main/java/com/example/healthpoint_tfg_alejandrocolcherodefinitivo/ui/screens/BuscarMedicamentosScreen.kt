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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.MedicamentosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarMedicamentosScreen(
    onBack: () -> Unit, // Callback de navegacion para salir de la pantalla
    viewModel: MedicamentosViewModel = viewModel() // Impplementacion del ViewModel
) {

    // Usamos collectAsState() convierten los StatesFlows del ViewModel en estados que Compose observa
    val query by viewModel.sentencia.collectAsState() // Texto de busqueda
    val resultados by viewModel.resultados.collectAsState() // Lista de sugerencias de nombre
    val loading by viewModel.loading.collectAsState() // Cargando la busqueda
    val error by viewModel.error.collectAsState() // Mensaje de error
    val detalle by viewModel.medicamentoDetalle.collectAsState() // Detalle del medicamento seleccionado

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Scaffold(
        containerColor = Color(0xFFF7F9FC), // Fondo blanco suave
        topBar = {
            TopAppBar(
                title = { Text("Buscar Medicamentos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = onPrimaryColor)},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = onPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor, // Azul cielo
                    titleContentColor = onPrimaryColor
                )
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
                        viewModel.actualizarQuery(it) // Actualiza el StateFlow de la consulta
                        viewModel.buscarSugerencia() }, // Dispara la busqueda o sugerencia
                    placeholder = { Text("Buscar por nombre (mínimo 3 letras)...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                // Indicadores de carga: mostramos una barra de progreso lineal mientras la informacion carga
                if (loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(16.dp))

                // Control de Flujo para la interfaz dinamica
                when {
                    error != null -> {
                        // Mostramos mensaje de error de conexion
                        Text("Error de conexion $error", color = MaterialTheme.colorScheme.error)
                    }

                    !loading && resultados.isEmpty() && query.length >= 3 -> {
                        // Mostramos mensaje de no encontrado después de buscar con suficientes caracteres.
                        Text("No se encontraron coincidencias para \"$query\".", style = MaterialTheme.typography.bodyMedium)
                    }

                    !loading && query.length < 3 -> {
                        // Mensaje de guía para el usuario.
                        Text("Introduce al menos 3 caracteres para buscar.", style = MaterialTheme.typography.bodySmall)
                    }

                    else -> {
                        // Muestra la lista de resultados
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
        onClick = onClick, // El click disparala acción proporcionada
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
        }
    }
}
