package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.theme.HealthPointTFGALEJANDROCOLCHERODEFINITIVOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthPointTFGALEJANDROCOLCHERODEFINITIVOTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> SplashScreen {
                        currentScreen = "login"
                    }

                    "login" -> LoginScreen(
                        onLoginSuccess = { currentScreen = "home" },
                        onCreateAccountClick = {
                            currentScreen = "register"
                        } // Aquí abrimos tu RegistrerScreen
                    )

                    "register" -> RegistrerScreen(  // <- nota el nombre que tienes
                        onRegisterSuccess = { currentScreen = "home" }
                    )
                }

            }
        }
    }
}