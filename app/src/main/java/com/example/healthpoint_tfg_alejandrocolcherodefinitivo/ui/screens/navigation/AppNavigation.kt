package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.BuscarMedicamentosScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.CentroMedicoScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.CitasPacienteScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.GestionarCitasScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.GestionarPacientesScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.GestionarTratamientosScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.HomeMedicoScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.HomePacienteScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.LoginScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.PerfilMedicoScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.PerfilPacientesScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.SplashScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.TratamientoPacienteScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.RegistrerScreen
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
    val CITA_PACIENTES_RUTA = "citas_pacientes/{idPaciente}"
    val TRATAMIENTO_PACIENTES_RUTA = "tratamiento_pacientes/{idPaciente}"
    val VER_PERFIL_PACIENTE_RUTA = "perfil_paciente/{idPaciente}"


    val GESTIONAR_CITAS_RUTA = "gestionar_citas/{idMedico}"
    val GESTIONAR_PACIENTES_RUTA = "gestionar_pacientes/{idMedico}"
    val GESTIONAR_TRATAMIENTOS_RUTA = "gestionar_tratamientos/{idMedico}"
    val VER_PERFIL_MEDICO_RUTA = "perfil_medico/{idMedico}"
    val VER_CENTRO_MEDICO_RUTA = "centro_medico/{idMedico}"
    val BUSCAR_MEDICAMENTOS_RUTA = "buscar_medicamentos/{idMedico}"


    NavHost(navController = navController, startDestination = SPLASH_RUTA) {
        // SPLASH SCREEN
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
            val idUsuario = backStackEntry.arguments?.getString("idPaciente") ?: ""
            HomePacienteScreen(
                idUsuario = idUsuario,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(HOME_PACIENTE_RUTA) { inclusive = true }
                    }
                },
                onVerCita = { navController.navigate("citas_pacientes/$idUsuario") },
                onVerTratamientos = { navController.navigate("tratamiento_pacientes/$idUsuario") },
                onVerPerfil = { navController.navigate("perfil_paciente/$idUsuario") },
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

        // CITA PACIENTES
        composable("citas_pacientes/{idPaciente}") {back ->
            val idPaciente = back.arguments?.getString("idPaciente") ?: ""
            CitasPacienteScreen(
                idPaciente = idPaciente,
                onBack = {
                navController.popBackStack() })
        }

        // GESTIONAR TRATAMIENTOS
        composable("$GESTIONAR_TRATAMIENTOS_RUTA/{idMedico}") {back ->
            val idMedico = back.arguments?.getString("idMedico") ?:""

            GestionarTratamientosScreen(
                idMedico = idMedico,
                onBack = {navController.popBackStack()}
            )
        }

        // TRATAMIENTO PACIENTES
        composable("tratamiento_pacientes/{idPaciente}") { back ->
            val idPaciente = back.arguments?.getString("idPaciente") ?:""

            TratamientoPacienteScreen(
                idPaciente = idPaciente,
                onBack = {navController.popBackStack()}
            )
        }

        // GESTIONAR CITAS PACIENTES
        composable("$GESTIONAR_CITAS_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""

            GestionarCitasScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() }
            )
        }

        // GESTIONAR PACIENTES
        composable("$GESTIONAR_PACIENTES_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            GestionarPacientesScreen(idMedico, onBack = { navController.popBackStack() })
        }

        // VER EL PERFIL DEL MEDICO
        composable("$VER_PERFIL_MEDICO_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            PerfilMedicoScreen(idMedico, onBack = { navController.popBackStack() })
        }

        // VER EL CENTRO MEDICO
        composable("$VER_CENTRO_MEDICO_RUTA/{idCentro}") { back ->
            val idCentro = back.arguments?.getString("idCentro") ?: ""
            CentroMedicoScreen(idCentro, onBack = { navController.popBackStack() })
        }

        // BUSCAR MEDICAMENTOS
        composable("$BUSCAR_MEDICAMENTOS_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            BuscarMedicamentosScreen(onBack = { navController.popBackStack() })
        }

        // VER PERFIL DEL PACIENTE RUTA
        composable(VER_PERFIL_PACIENTE_RUTA) {
            PerfilPacientesScreen(onBack = { navController.popBackStack() })
        }
    }
}