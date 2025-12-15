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

// Constantes de Argumentos
const val ARG_ID_PACIENTE = "idPaciente"
const val ARG_ID_MEDICO = "idMedico"
const val ARG_ID_CENTRO = "idCentro"

// Rutas Base sin argumentos
const val SPLASH_RUTA = "splash_screen"
const val LOGIN_RUTA = "login_screen"
const val REGISTER_RUTA = "register_screen"

// Rutas Base con Argumentos
const val HOME_PACIENTE_BASE = "home_paciente"
const val VER_PERFIL_PACIENTE_BASE = "perfil_paciente"
const val CITAS_PACIENTES_BASE = "citas_pacientes"
const val TRATAMIENTO_PACIENTES_BASE = "tratamiento_pacientes"
const val SOLICITAR_CITA_BASE = "solicitar_cita"
const val ESTADO_SOLICITUDES_PACIENTE_BASE = "estado_solicitudes_paciente"

const val HOME_MEDICO_BASE = "home_medico"
const val GESTIONAR_CITAS_BASE = "gestionar_citas"
const val GESTIONAR_PACIENTES_BASE = "gestionar_pacientes"
const val GESTIONAR_TRATAMIENTOS_BASE = "gestionar_tratamientos"
const val VER_PERFIL_MEDICO_BASE = "perfil_medico"
const val VER_CENTRO_MEDICO_BASE = "centro_medico"
const val BUSCAR_MEDICAMENTOS_BASE = "buscar_medicamentos"
const val SOLICITUDES_MEDICO_BASE = "solicitudes_medico"

// Rutas completas para composable con argumentos
const val HOME_PACIENTE_RUTA = "$HOME_PACIENTE_BASE/{$ARG_ID_PACIENTE}"
const val VER_PERFIL_PACIENTE_RUTA = "$VER_PERFIL_PACIENTE_BASE/{$ARG_ID_PACIENTE}"
const val CITAS_PACIENTES_RUTA = "$CITAS_PACIENTES_BASE/{$ARG_ID_PACIENTE}"
const val TRATAMIENTO_PACIENTES_RUTA = "$TRATAMIENTO_PACIENTES_BASE/{$ARG_ID_PACIENTE}"
const val SOLICITAR_CITA_RUTA = "$SOLICITAR_CITA_BASE/{$ARG_ID_PACIENTE}"
const val ESTADO_SOLICITUDES_PACIENTE_RUTA = "$ESTADO_SOLICITUDES_PACIENTE_BASE/{$ARG_ID_PACIENTE}"

const val HOME_MEDICO_RUTA = "$HOME_MEDICO_BASE/{$ARG_ID_MEDICO}"
const val GESTIONAR_CITAS_RUTA = "$GESTIONAR_CITAS_BASE/{$ARG_ID_MEDICO}"
const val GESTIONAR_PACIENTES_RUTA = "$GESTIONAR_PACIENTES_BASE/{$ARG_ID_MEDICO}"
const val GESTIONAR_TRATAMIENTOS_RUTA = "$GESTIONAR_TRATAMIENTOS_BASE/{$ARG_ID_MEDICO}"
const val VER_PERFIL_MEDICO_RUTA = "$VER_PERFIL_MEDICO_BASE/{$ARG_ID_MEDICO}"
const val VER_CENTRO_MEDICO_RUTA = "$VER_CENTRO_MEDICO_BASE/{$ARG_ID_CENTRO}"
const val BUSCAR_MEDICAMENTOS_RUTA = "$BUSCAR_MEDICAMENTOS_BASE/{$ARG_ID_MEDICO}"
const val SOLICITUDES_MEDICO_RUTA = "$SOLICITUDES_MEDICO_BASE/{$ARG_ID_MEDICO}"


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val pacienteNavArguments = listOf(navArgument(ARG_ID_PACIENTE) { type = NavType.StringType })

    val medicoNavArguments = listOf(navArgument(ARG_ID_MEDICO) { type = NavType.StringType })

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
            val idUsuarioAuth = backStackEntry.arguments?.getString("idMedico") ?: ""
            HomeMedicoScreen(
                idUsuario = idUsuarioAuth,
                viewModel = viewModel(),
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LOGIN_RUTA) {
                        popUpTo(HOME_MEDICO_RUTA) { inclusive = true }
                    }
                },
                onGestionarPacientes = { navController.navigate("$GESTIONAR_PACIENTES_BASE/$idUsuarioAuth") },
                onGestionarCitas = { navController.navigate("$GESTIONAR_CITAS_BASE/$idUsuarioAuth") },
                onGestionarTratamientos = { navController.navigate("$GESTIONAR_TRATAMIENTOS_BASE/$idUsuarioAuth") },
                onVerPerfil = { navController.navigate("$VER_PERFIL_MEDICO_BASE/$idUsuarioAuth") },
                onBuscarMedicamentos = {navController.navigate("$BUSCAR_MEDICAMENTOS_BASE/$idUsuarioAuth") },
                onGestionarSolicitudes = { idMedicoNormalizado ->
                    navController.navigate("$SOLICITUDES_MEDICO_BASE/$idMedicoNormalizado")
                }
            )
        }

        // CITA PACIENTES
        composable(
            route = CITAS_PACIENTES_RUTA,
            arguments = pacienteNavArguments
            ) {backStackEntry ->
            val idPaciente = backStackEntry.arguments?.getString(ARG_ID_PACIENTE) ?: ""
            CitasPacienteScreen(
                idPaciente = idPaciente,
                onBack = {
                navController.popBackStack() })
        }

        // GESTIONAR TRATAMIENTOS
        composable(
            route = GESTIONAR_TRATAMIENTOS_RUTA,
            arguments = medicoNavArguments) { backStackEntry ->
            val idMedico = backStackEntry.arguments?.getString(ARG_ID_MEDICO) ?:""
            GestionarTratamientosScreen(
                idMedico = idMedico,
                onBack = {navController.popBackStack()}
            )
        }

        // TRATAMIENTO PACIENTES
        composable(
            route = TRATAMIENTO_PACIENTES_RUTA,
            arguments = pacienteNavArguments) { backStackEntry ->
            val idPaciente = backStackEntry.arguments?.getString(ARG_ID_PACIENTE) ?: ""
            TratamientoPacienteScreen(
                idPaciente = idPaciente,
                onBack = {navController.popBackStack()}
            )
        }

        // GESTIONAR CITAS PACIENTES
        composable(
            route = GESTIONAR_CITAS_RUTA,
            arguments = medicoNavArguments) { backStackEntry ->
            val idMedico = backStackEntry.arguments?.getString(ARG_ID_MEDICO) ?: ""
            GestionarCitasScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() }
            )
        }

        // GESTIONAR PACIENTES
        composable(
            route = GESTIONAR_PACIENTES_RUTA,
            arguments = medicoNavArguments) { backStackEntry ->
            val idPaciente = backStackEntry.arguments?.getString(ARG_ID_PACIENTE) ?:""
            GestionarPacientesScreen(
                idPaciente = idPaciente,
                onBack = { navController.popBackStack() })
        }

        // VER EL PERFIL DEL MEDICO
        composable(
            route = VER_PERFIL_MEDICO_RUTA,
            arguments = medicoNavArguments) { backStackEntry ->
            val idMedico = backStackEntry.arguments?.getString(ARG_ID_MEDICO) ?: ""
            PerfilMedicoScreen(
                idMedico = idMedico,
                onBack = { navController.popBackStack() })
        }

        // VER EL CENTRO MEDICO
        composable(
            route = VER_CENTRO_MEDICO_RUTA,
            arguments = listOf(navArgument(ARG_ID_CENTRO) { type = NavType.StringType })
        ){ backStackEntry ->
            val idCentro = backStackEntry.arguments?.getString(ARG_ID_CENTRO) ?: ""
            CentroMedicoScreen(
                idCentro,
                onBack = { navController.popBackStack() })
        }

        // BUSCAR MEDICAMENTOS
        composable(
            route = BUSCAR_MEDICAMENTOS_RUTA,
            arguments = medicoNavArguments) { backStackEntry ->
            val idMedico = backStackEntry.arguments?.getString(ARG_ID_MEDICO) ?: ""
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

        // SOLICITUDES DEL PACIENTE
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