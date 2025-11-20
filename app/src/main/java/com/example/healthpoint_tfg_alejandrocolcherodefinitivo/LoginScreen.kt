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
    onLoginSuccess: (Any?) -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            // Nombre de la app
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

            Spacer(modifier = Modifier.height(24.dp))

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