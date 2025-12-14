package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.navigation.AppNavigation
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.theme.HealthPointTFGALEJANDROCOLCHERODEFINITIVOTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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