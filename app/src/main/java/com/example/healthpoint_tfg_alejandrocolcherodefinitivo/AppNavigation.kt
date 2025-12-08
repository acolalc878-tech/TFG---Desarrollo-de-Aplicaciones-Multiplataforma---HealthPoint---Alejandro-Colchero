package com.example.healthpoint_tfg_alejandrocolcherodefinitivo


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Rutas como constantes para navegar entre pantallas
    val SPLASH_RUTA = "splash_screen"
    val LOGIN_RUTA = "login_screen"
    val REGISTER_RUTA = "register_screen"
    val HOME_PACIENTE_RUTA = "home_paciente/{idPaciente}"
    val HOME_MEDICO_RUTA = "home_medico/{idMedico}"

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
            SplashScreen(onFinished = {
                navController.navigate(LOGIN_RUTA) {
                    popUpTo(SPLASH_RUTA) { inclusive = true }
                }
            })
        }

        // LOGIN
        composable(LOGIN_RUTA) {
            LoginScreen(
                onLoginSuccess = { role, idUsuario ->
                    when (role) {

                        "Paciente" -> {
                            navController.navigate("home_paciente/$idUsuario") {
                                popUpTo(LOGIN_RUTA) { inclusive = true }
                            }
                        }

                        "Medico" -> {
                            navController.navigate("home_medico/$idUsuario") {
                                popUpTo(LOGIN_RUTA) { inclusive = true }
                            }
                        }
                    }
                },

                onCreateAccountClick = { navController.navigate(REGISTER_RUTA) }
            )
        }

        // REGISTER
        composable(REGISTER_RUTA) {
            RegistrerScreen(
                onRegisterSuccess = {
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(REGISTER_RUTA) { inclusive = true }
                    }
                }
            )
        }

        // HOME PACIENTE
        composable(HOME_PACIENTE_RUTA) { backStackEntry ->
            val idPaciente = backStackEntry.arguments?.getString("idPaciente") ?: ""
            HomePacienteScreen(
                idPaciente = idPaciente,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo("login_screen") { inclusive = true }
                    }
                },
                onVerCita = { navController.navigate("citas_pacientes") },
                onVerTratamientos = { navController.navigate("tratamiento_pacientes") },
                onVerPerfil = { navController.navigate("perfil_paciente") },
                onVerHistorial = {}
            )
        }


        // HOME MEDICO
        composable(HOME_MEDICO_RUTA) { backStackEntry ->

            val idMedico = backStackEntry.arguments?.getString("idMedico") ?: ""

            HomeMedicoScreen(
                idUsuario = idMedico,
                viewModel = viewModel(),

                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(HOME_MEDICO_RUTA) { inclusive = true }
                    }
                },

                onGestionarPacientes = {
                    navController.navigate("$GESTIONAR_PACIENTES_RUTA/$idMedico")
                },

                onGestionarCitas = {
                    navController.navigate("$GESTIONAR_CITAS_RUTA/$idMedico")
                },

                onGestionarTratamientos = {
                    navController.navigate("$GESTIONAR_TRATAMIENTOS_RUTA/$idMedico")
                },

                onVerCentro = { idCentro ->
                    navController.navigate("$VER_CENTRO_MEDICO_RUTA/$idCentro")
                },

                onVerPerfil = {
                    navController.navigate("$VER_PERFIL_MEDICO_RUTA/$idMedico")
                },

                onBuscarMedicamentos = {
                    navController.navigate("$BUSCAR_MEDICAMENTOS_RUTA/$idMedico")
                }
            )
        }

        composable("$GESTIONAR_CITAS_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""

            GestionarCitasScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() }
            )
        }

        composable("$GESTIONAR_PACIENTES_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            GestionarPacientesScreen(idMedico, onBack = { navController.popBackStack() })
        }

        composable("$VER_PERFIL_MEDICO_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            PerfilMedicoScreen(idMedico, onBack = { navController.popBackStack() })
        }

        composable("$VER_CENTRO_MEDICO_RUTA/{idCentro}") { back ->
            val idCentro = back.arguments?.getString("idCentro") ?: ""
            CentroMedicoScreen(idCentro, onBack = { navController.popBackStack() })
        }

        composable("$BUSCAR_MEDICAMENTOS_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            BuscarMedicamentosScreen(onBack = { navController.popBackStack() })
        }

        composable(VER_PERFIL_PACIENTE_RUTA) {
            PerfilPacientesScreen(onBack = { navController.popBackStack() })

        }
    }
}