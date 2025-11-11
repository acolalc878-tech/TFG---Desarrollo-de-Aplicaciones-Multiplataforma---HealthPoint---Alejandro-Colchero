package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegistrerScreen(
    viewModel: RegistrerScreenViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {}
) {

    var role by remember { mutableStateOf("Paciente") }

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Campos exclusivos del Médico
    var numColegiado by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2196F3)
            )

            Spacer(modifier= Modifier.height(16.dp))

            // Campos comunes
            // Campo para el nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))

            // Campo para los apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))

            // Campo para el email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))

            // Campo para el telefono
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Número de Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))

            // Campo para la fecha de nacimiento
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))

            // Campo para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier= Modifier.height(8.dp))


            // Seleccion de rol: Botones de rol
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { role = "Paciente" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Paciente") Color(0xFF2196F3) else Color.LightGray
                    ), modifier = Modifier.weight(1f)
                ) {
                    Text("Paciente")
                }

                Spacer(modifier= Modifier.height(16.dp))

                Button(
                    onClick = {role = "Medico"},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "Medico") Color(0xFF2196F3) else Color.LightGray
                    ), modifier = Modifier.weight(1f)
                ) {
                    Text("Médico")
                }
            }

            Spacer(modifier= Modifier.height(24.dp))

            // Campos específicos para los médicos
            if (role == "Medico") {
                OutlinedTextField(value = numColegiado, onValueChange = { numColegiado = it }, label = { Text("Número Colegiado") })
                OutlinedTextField(value = especialidad, onValueChange = { especialidad = it }, label = { Text("Especialidad") })
                OutlinedTextField(value = horario, onValueChange = { horario = it }, label = { Text("Horario") })
            }

            Spacer(Modifier.height(24.dp))

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red)
                Spacer(modifier= Modifier.height(8.dp))
            }

            // Boton para crear la cuenta
            Button(
                onClick = {
                    viewModel.registrerUser(
                        nombre = nombre,
                        apellidos = apellidos,
                        email = email,
                        telefono = telefono,
                        password = password,
                        role = role,
                        fechaNacimiento = fechaNacimiento,
                        onSuccess = { onRegisterSuccess() },
                        onError = { errorMessage = it }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Registrate", color = Color.White)
            }

            // Circulo de carga
            if(viewModel.isLoading.value) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}
