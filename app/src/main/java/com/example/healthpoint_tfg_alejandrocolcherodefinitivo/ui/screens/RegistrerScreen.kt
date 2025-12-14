package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.RegistrerScreenViewModel

@Composable
fun RegistrerScreen(
    viewModel: RegistrerScreenViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {}
) {
    val isLoading by remember { derivedStateOf { viewModel.isLoading.value}}
    val lastError by remember { derivedStateOf { viewModel.lastError.value}}

    // Campos del formulario del login
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Paciente") }

    // Campos exclusivos del Médico
    var numColegiado by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var idCentroMedico by remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)) {

            Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = apellidos, onValueChange = { apellidos = it },
                label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = email, onValueChange = { email = it },
                label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = telefono, onValueChange = { telefono = it },
                label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de nacimiento") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = password, onValueChange = { password = it },
                label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())


            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { role = "Paciente" }, colors = ButtonDefaults.buttonColors(containerColor =
                if (role=="Paciente") MaterialTheme.colorScheme.primary else Color.LightGray),
                    modifier = Modifier.weight(1f)) {
                    Text("Paciente")
                }

                Button(onClick = { role = "Medico" }, colors = ButtonDefaults.buttonColors(containerColor =
                if (role=="Medico") MaterialTheme.colorScheme.primary else Color.LightGray),
                    modifier = Modifier.weight(1f)) {
                    Text("Médico")
                }
            }

            if (role == "Medico") {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = especialidad, onValueChange = { especialidad = it},
                    label = { Text("Especialidad")}, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = numColegiado, onValueChange = { numColegiado = it},
                    label = { Text("NºColegiado")}, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = horario, onValueChange = { horario = it},
                    label = { Text("Horario")}, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = idCentroMedico, onValueChange = { idCentroMedico = it },
                    label = { Text("ID Centro Médico (opcional)") }, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.registerUser(
                    nombre = nombre,
                    apellidos = apellidos,
                    edad = edad,
                    email = email,
                    telefono = telefono,
                    password = password,
                    role = role,
                    fechaNacimiento = fechaNacimiento,
                    idCentroMedico = idCentroMedico.takeIf { it.isNotBlank() },
                    especialidad = especialidad,
                    numColegiado = numColegiado,
                    horario = horario,
                    onSuccess = {
                        onRegisterSuccess()
                    },
                    onError = { msg ->
                    }
                )
            },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()) {
                Text(if (!isLoading) "Crear cuenta" else "Creando cuenta...")
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                CircularProgressIndicator()
            }
            lastError?.let { Text(it, color = Color.Red) }
        }
    }
}
