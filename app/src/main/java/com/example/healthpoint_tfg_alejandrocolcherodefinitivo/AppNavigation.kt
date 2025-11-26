package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Rutas como constantes para navegar entre pantallas
    val SPLASH_RUTA = "splash_screen"
    val LOGIN_RUTA = "login_screen"
    val REGISTER_RUTA = "register_screen"
    val HOME_PACIENTE_RUTA = "home_paciente"
    val HOME_MEDICO_RUTA = "home_medico"
    val CITA_PACIENTES_RUTA = "citas_pacientes"
    val TRATAMIENTO_PACIENTES_RUTA = "tratamiento_pacientes"
    val VER_PERFIL_PACIENTE_RUTA = "perfil_paciente"

    val GESTIONAR_CITAS_RUTA = "gestionar_citas"
    val GESTIONAR_PACIENTES_RUTA = "gestionar_pacientes"
    val GESTIONAR_TRATAMIENTOS_RUTA = "gestionar_tratamientos"
    val VER_PERFIL_MEDICO_RUTA = "perfil_medico"
    val VER_CENTRO_MEDICO_RUTA = "centro_medico"
    val BUSCAR_MEDICAMENTOS_RUTA = "buscar_medicamentos"


    NavHost(navController = navController, startDestination = SPLASH_RUTA) {
        // Splash Screen
        composable(SPLASH_RUTA) {
            SplashScreen(onFinished = { navController.navigate(LOGIN_RUTA) {
                    popUpTo(SPLASH_RUTA) { inclusive = true }}})
        }

        // Login de inicio de sesion
        composable(LOGIN_RUTA) {
            LoginScreen(
                onLoginSuccess = { role ->
                    when (role) {
                        "Paciente" -> {
                            navController.navigate(HOME_PACIENTE_RUTA) {
                                popUpTo(LOGIN_RUTA) { inclusive = true }
                            }
                        }
                        "Medico" -> {
                            navController.navigate(HOME_MEDICO_RUTA) {
                                popUpTo(LOGIN_RUTA) { inclusive = true }
                            }
                        }
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(REGISTER_RUTA)
                }
            )
        }

        // REGISTER
        composable(REGISTER_RUTA){
            RegistrerScreen(
                onRegisterSuccess = {
                    // Al apuntar a login aseguramos que el usuario no queda logueado automáticamente
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(REGISTER_RUTA) { inclusive = true }
                    }
                }
            )
        }

        // Home Pacientes
        composable(HOME_PACIENTE_RUTA) {
            HomePacienteScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {popUpTo(HOME_PACIENTE_RUTA) {inclusive = true} }
                },
                onVerCita = {navController.navigate(CITA_PACIENTES_RUTA)},
                onVerTratamientos = {navController.navigate(TRATAMIENTO_PACIENTES_RUTA)},
                onVerPerfil = {navController.navigate(VER_PERFIL_PACIENTE_RUTA)},
                onVerHistorial = {/**/}
            )
        }

        // Home Medico
        composable(HOME_MEDICO_RUTA) {
            HomeMedicoScreen(
                idUsuario = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                viewModel = viewModel(),
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(HOME_MEDICO_RUTA) { inclusive = true }
                    }
                },
                onGestionarPacientes = { idMedico ->
                    navController.navigate("$GESTIONAR_PACIENTES_RUTA/$idMedico")
                },
                onGestionarCitas = { idMedico ->
                    navController.navigate("$GESTIONAR_CITAS_RUTA/$idMedico")
                },
                onGestionarTratamientos = { idMedico ->
                    navController.navigate("$GESTIONAR_TRATAMIENTOS_RUTA/$idMedico")
                },
                onVerCentro = { idCentro ->
                    navController.navigate("$VER_CENTRO_MEDICO_RUTA/$idCentro")
                },
                onVerPerfil = { idMedico ->
                    navController.navigate("$VER_PERFIL_MEDICO_RUTA/$idMedico")
                },
                onBuscarMedicamentos = { idMedico ->
                    navController.navigate("$BUSCAR_MEDICAMENTOS_RUTA/$idMedico")
                }
            )
        }

        composable(CITA_PACIENTES_RUTA) {
            CitasPacienteScreen(onBack = { navController.popBackStack() })
        }

        composable(TRATAMIENTO_PACIENTES_RUTA) {
            TratamientoPacienteScreen (onBack = { navController.popBackStack() })
        }

        composable(VER_PERFIL_PACIENTE_RUTA) {
            PerfilPacientesScreen (onBack = { navController.popBackStack() })
        }

    }
}
