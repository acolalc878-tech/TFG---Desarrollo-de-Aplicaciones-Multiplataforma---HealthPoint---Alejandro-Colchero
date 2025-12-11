package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.R
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = viewModel(),
    onLoginSuccess: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = primaryColor.copy(alpha =  0.1f) // Fondo claro
    ) {padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Imagen y logotipo en la parte superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(primaryColor),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Image(
                        painter = painterResource(id = R.drawable.logo4),
                        contentDescription = "Logo HealthPoint",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "HealthPoint",
                        style = MaterialTheme.typography.headlineLarge,
                        color = surfaceColor,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Portal de Acceso para Profesionales y Pacientes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = surfaceColor.copy(alpha = 0.8f)
                    )
                }
            }

            // Formulario principal en una card elevada
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .offset(y = (-40).dp) // Sobreponemos un poco sobre el encabezado
                    .weight(0.6f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Campos de texto a rellenar
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Filled.Check else Icons.Filled.Clear
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = if (passwordVisible) "Ocultar Contraseña" else "Mostrar Contraseña"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    //Boton de iniciar sesion
                    Button(
                        onClick = {
                            errorMessage = ""
                            isLoading = true
                            // Logica de autenticacion
                            viewModel.signInWithEmailAndPassword(
                                email = email,
                                password = password,
                                onLoginSuccess = { role, idUsuario ->
                                    isLoading = false
                                    onLoginSuccess(role, idUsuario)
                                },
                                onError = { msg ->
                                    isLoading = false
                                    errorMessage = msg
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Cargando..." else "Iniciar sesión")
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            errorMessage,
                            color = errorColor,
                            style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Boton de crear cuenta
                    Text(
                        text = "¿No tienes cuenta? Crear Cuenta",
                        modifier = Modifier
                        .clickable(onClick = onCreateAccountClick),
                        color = primaryColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
