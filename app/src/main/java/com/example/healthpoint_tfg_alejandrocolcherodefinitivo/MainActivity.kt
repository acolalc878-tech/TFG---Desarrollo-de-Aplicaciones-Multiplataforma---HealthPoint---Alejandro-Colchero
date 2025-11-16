package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.theme.HealthPointTFGALEJANDROCOLCHERODEFINITIVOTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Conectamos con Firebase
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            HealthPointTFGALEJANDROCOLCHERODEFINITIVOTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navegación principal de la app
                    AppNavigation()
                }
            }
        }
    }
}