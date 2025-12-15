package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CentroMedicoScreen (
    idCentro: String, // identificador único del centro médico a mostrar.
    onBack: () -> Unit // función para volver a la pantalla anterior.
) {
    // Scaffold: Proporciona la estructura básica (barra superior, cuerpo, etc.).
    Scaffold(
        topBar = {
            // Barra superior de la aplicación (TopAppBar).
            TopAppBar(
                title = { Text("Centro Médico") } ,
                navigationIcon = {
                    // Boton con icono para volver hacia atrás
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        // Contenido Principal (Body):
        Column(
            // aplicamos el padding del Scaffold para que el contenido no se solape con la barra superior
            Modifier
                .padding(padding)
                .fillMaxSize(),
            // centramos el contenido horizontal y verticalmente
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text("Información del centro médico $idCentro")
        }
    }
}