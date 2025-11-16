package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Definicion de constantes como rutas de String
    val SPLASH_RUTA = "splash_screen"
    val LOGIN_RUTA = "login_screen"
    val REGISTER_RUTA = "register_screen"
    val HOME_RUTA = "home_screen"
    val HOME_PACIENTE_RUTA = "home_paciente"
    val HOME_MEDICO_RUTA = "home_medico"

    NavHost(
        navController = navController,
        startDestination = SPLASH_RUTA // Hacemos que primero salga el Splash Screen
    ) {

        // Splash Screen
        composable(SPLASH_RUTA) {
            SplashScreen(onFinished = {
                navController.navigate(LOGIN_RUTA) {
                    popUpTo(SPLASH_RUTA) { inclusive = true }
                }
            })
        }

        // Login Screen
        composable(LOGIN_RUTA) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(HOME_RUTA) {
                        popUpTo(LOGIN_RUTA) {inclusive = true} // Borramos el Login de la pila
                    }
                },
                onCreateAccountClick = {
                    // Click en Crear Cuenta
                    navController.navigate(REGISTER_RUTA)
                }
            )
        }

        // Register
        composable(REGISTER_RUTA){
            RegistrerScreen(
                onRegisterSuccess = {
                    navController.navigate(HOME_RUTA) {
                        popUpTo(REGISTER_RUTA) {inclusive = true} // Borramos el register de la pila
                    }
                }
            )
        }

        // HOME
        composable(HOME_RUTA){
            Text("HealthPoint de Alejandro")
        }
    }
}