package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.BuscarMedicamentosScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.CentroMedicoScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.CitasPacienteScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.EstadoSolicitudesPacienteScreen
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
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.RegistrerScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.SolicitudCitaScreen
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.SolicitudesCitaMedicoScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Argumentos
    val ARG_ID_PACIENTE = "idPaciente"
    val ARG_ID_MEDICO = "idMedico"
    val ARG_ID_CENTRO = "idCentro"

    // Rutas Base sin argumentos
    val SPLASH_RUTA = "splash_screen"
    val LOGIN_RUTA = "login_screen"
    val REGISTER_RUTA = "register_screen"
    val HOME_PACIENTE_BASE = "home_paciente"
    val HOME_MEDICO_BASE = "home_medico"
    val VER_PERFIL_PACIENTE_BASE = "perfil_paciente"
    val CITAS_PACIENTES_BASE = "citas_pacientes"
    val TRATAMIENTO_PACIENTES_BASE = "tratamiento_pacientes"
    val SOLICITAR_CITA_BASE = "solicitar_cita"
    val ESTADO_SOLICITUDES_PACIENTE_BASE = "estado_solicitudes_paciente"

    val GESTIONAR_CITAS_BASE = "gestionar_citas"
    val GESTIONAR_PACIENTES_BASE = "gestionar_pacientes"
    val GESTIONAR_TRATAMIENTOS_BASE = "gestionar_tratamientos"
    val VER_PERFIL_MEDICO_BASE = "perfil_medico"
    val VER_CENTRO_MEDICO_BASE = "centro_medico"
    val BUSCAR_MEDICAMENTOS_BASE = "buscar_medicamentos"
    val SOLICITUDES_MEDICO_BASE = "solicitudes_medico"

    // Rutas completas para composable con argumentos
    val HOME_PACIENTE_RUTA = "$HOME_PACIENTE_BASE/{$ARG_ID_PACIENTE}"
    val VER_PERFIL_PACIENTE_RUTA = "$VER_PERFIL_PACIENTE_BASE/{$ARG_ID_PACIENTE}"
    val SOLICITAR_CITA_RUTA = "$SOLICITAR_CITA_BASE/{$ARG_ID_PACIENTE}"
    val ESTADO_SOLICITUDES_PACIENTE_RUTA = "$ESTADO_SOLICITUDES_PACIENTE_BASE/{$ARG_ID_PACIENTE}"

    val HOME_MEDICO_RUTA = "$HOME_MEDICO_BASE/{$ARG_ID_MEDICO}"
    val GESTIONAR_CITAS_RUTA = "$GESTIONAR_CITAS_BASE/{$ARG_ID_MEDICO}"
    val GESTIONAR_PACIENTES_RUTA = "$GESTIONAR_PACIENTES_BASE/{$ARG_ID_MEDICO}"
    val GESTIONAR_TRATAMIENTOS_RUTA = "$GESTIONAR_TRATAMIENTOS_BASE/{$ARG_ID_MEDICO}"
    val VER_PERFIL_MEDICO_RUTA = "$VER_PERFIL_MEDICO_BASE/{$ARG_ID_MEDICO}"
    val VER_CENTRO_MEDICO_RUTA = "$VER_CENTRO_MEDICO_BASE/{$ARG_ID_CENTRO}"
    val BUSCAR_MEDICAMENTOS_RUTA = "$BUSCAR_MEDICAMENTOS_BASE/{$ARG_ID_MEDICO}"
    val SOLICITUDES_MEDICO_RUTA = "$SOLICITUDES_MEDICO_BASE/{$ARG_ID_MEDICO}"

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
                onVerCita = { navController.navigate("$CITAS_PACIENTES_BASE/$idUsuario") },
                onVerTratamientos = { navController.navigate("$TRATAMIENTO_PACIENTES_BASE/$idUsuario") },
                onVerPerfil = { navController.navigate("$VER_PERFIL_PACIENTE_BASE/$idUsuario") },
                onSolicitarCita = { navController.navigate("$SOLICITAR_CITA_BASE/$idUsuario") },
                onVerEstadoSoliciatudes = { navController.navigate("$ESTADO_SOLICITUDES_PACIENTE_BASE/$idUsuario")}
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
                onGestionarPacientes = { navController.navigate("$GESTIONAR_PACIENTES_BASE/$idMedico") },
                onGestionarCitas = { navController.navigate("$GESTIONAR_CITAS_BASE/$idMedico") },
                onGestionarTratamientos = { navController.navigate("$GESTIONAR_TRATAMIENTOS_BASE/$idMedico") },
                onVerPerfil = { navController.navigate("$VER_PERFIL_MEDICO_BASE/$idMedico") },
                onBuscarMedicamentos = {navController.navigate("$BUSCAR_MEDICAMENTOS_BASE/$idMedico") },
                onGestionarSolicitudes = { navController.navigate("$SOLICITUDES_MEDICO_BASE/$idMedico")}
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
            GestionarPacientesScreen(
                onBack = { navController.popBackStack() })
        }

        // VER EL PERFIL DEL MEDICO
        composable("$VER_PERFIL_MEDICO_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            PerfilMedicoScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() })
        }

        // VER EL CENTRO MEDICO
        composable("$VER_CENTRO_MEDICO_RUTA/{idCentro}") { back ->
            val idCentro = back.arguments?.getString("idCentro") ?: ""
            CentroMedicoScreen(
                idCentro,
                onBack = { navController.popBackStack() })
        }

        // BUSCAR MEDICAMENTOS
        composable("$BUSCAR_MEDICAMENTOS_RUTA/{idMedico}") { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            BuscarMedicamentosScreen(
                onBack = { navController.popBackStack() })
        }

        // VER PERFIL DEL PACIENTE RUTA
        composable(VER_PERFIL_PACIENTE_RUTA) {
            PerfilPacientesScreen(
                onBack = { navController.popBackStack() })
        }

        // SOLICITAR CITA (PACIENTE)
        composable(SOLICITAR_CITA_RUTA) {back ->
            val idPaciente = back.arguments?.getString("idPaciente") ?: ""
            SolicitudCitaScreen(
                idPaciente = idPaciente,
                onBack = {navController.popBackStack()},
                onSolicitudEnviada = { id ->
                    navController.navigate("$ESTADO_SOLICITUDES_PACIENTE_BASE/$id") {
                        popUpTo(SOLICITAR_CITA_RUTA) { inclusive = true }
                    }
                }
            )
        }

        // SOLICITUDES DE CITA PARA EL MEDICO
        composable(SOLICITUDES_MEDICO_RUTA) { back ->
            val idMedico = back.arguments?.getString("idMedico") ?: ""
            SolicitudesCitaMedicoScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ESTADO_SOLICITUDES_PACIENTE_RUTA,
            arguments = listOf(navArgument(ARG_ID_PACIENTE) { type = NavType.StringType })
        ) { backStackEntry ->
            val idPaciente = backStackEntry.arguments?.getString(ARG_ID_PACIENTE) ?: ""
            EstadoSolicitudesPacienteScreen(
                idPaciente = idPaciente,
                onBack = { navController.popBackStack() }
            )
        }
    }
}