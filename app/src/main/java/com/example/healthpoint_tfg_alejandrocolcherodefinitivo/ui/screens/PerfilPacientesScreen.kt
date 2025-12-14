package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Usuario
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilPacientesScreen(
    viewModel: PerfilViewModel = viewModel(),
    onBack: () -> Unit
) {
    val usuario by viewModel.usuario.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensajeGuardado.collectAsState()

    LaunchedEffect(Unit) { viewModel.cargarPerfil() }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        usuario?.let { u ->
            var nombre by remember { mutableStateOf(u.nombre) }
            var apellido by remember { mutableStateOf(u.apellidos) }
            var telefono by remember { mutableStateOf(u.telefono) }
            var fechaNac by remember { mutableStateOf(u.fechaNacimiento) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it },
                        label = { Text("Nombre") })

                    OutlinedTextField(value = apellido, onValueChange = { apellido = it },
                        label = { Text("Apellido") })

                    OutlinedTextField(value = telefono, onValueChange = { telefono = it },
                        label = { Text("Teléfono") })

                    OutlinedTextField(value = fechaNac, onValueChange = { fechaNac = it },
                        label = { Text("Fecha de nacimiento") })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.guardarCambiosPerfil(
                            Usuario(
                                Id_usuario = u.Id_usuario,
                                nombre = nombre,
                                apellidos = apellido,
                                edad = u.edad,
                                email = u.email,
                                telefono = telefono,
                                rol = u.rol,
                                fechaNacimiento = fechaNac
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }

                mensaje?.let { Text(it) }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
