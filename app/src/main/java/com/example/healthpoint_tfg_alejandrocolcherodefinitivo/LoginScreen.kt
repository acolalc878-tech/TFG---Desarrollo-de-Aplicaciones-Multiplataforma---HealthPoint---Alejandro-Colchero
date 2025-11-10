package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.interaction.MutableInteractionSource


@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val selectedRole by viewModel.selectedRole.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "HealthPoint",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xDD2196F3)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo del correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostramos un mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Seleccion de rol: "Paciente" o "Medico"
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewModel.selectedRole("Paciente") },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (selectedRole == "Paciente") Color(0xFF2196F3) else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Paciente")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { viewModel.selectedRole("Médico") },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (selectedRole == "Médico") Color(0xFF2196F3) else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Médico")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Boton para iniciar sesion
            Button(
                onClick = {
                    viewModel.signInWithEmailAndPassword(
                        email, password, onLoginSuccess
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace para crear cuenta
            Text(
                text = "¿No tienes cuenta? Crear cuenta",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current
                    ) {
                        onCreateAccountClick()
                    },
                color = Color(0xFF2196F3),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}