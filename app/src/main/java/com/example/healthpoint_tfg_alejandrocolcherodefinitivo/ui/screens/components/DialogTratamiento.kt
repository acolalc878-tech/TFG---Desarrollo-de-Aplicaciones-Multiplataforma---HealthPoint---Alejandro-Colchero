package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Cita
import com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.Tratamiento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTratamiento(
    titulo: String,
    tratamientoInicial: Tratamiento,
    citasDisponibles: List<Cita>,
    onDismiss: () -> Unit,
    onSubmit: (Tratamiento) -> Unit
){
    // Estados para los campos del formulario
    val descripcion = remember { mutableStateOf(tratamientoInicial.descripcion) }
    val duracion = remember { mutableStateOf(tratamientoInicial.duracionDias.toString()) }
    val indicaciones = remember { mutableStateOf(tratamientoInicial.indicaciones) }

    // Estado para el Id de la cita seleccionaday la visibilidad del menú
    var idCitaSeleccionada by remember { mutableStateOf(tratamientoInicial.Id_cita)}
    var expanded by remember { mutableStateOf(false) }

    // Encuentra la cita seleccionada para mostrarla en el campo de texto
    val citaActual = citasDisponibles.find {it.id_cita == idCitaSeleccionada}
    val citaDisplay = if (citaActual != null) {
        "${citaActual.fecha} ${citaActual.hora} - Paciente: ${citaActual.id_usuario.take(4)}..."
    } else {
        "Seleccionar Cita..."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                // DropBox para seleccionar la cita
                Box(Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = citaDisplay,
                            onValueChange = {/*Solo lectura*/},
                            readOnly = true,
                            label = {Text("Cita para asignar tratamiento")},
                            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {expanded = false}
                        ) {
                            citasDisponibles.forEach{ cita ->
                                // Texto con interfaz amigable en el desplegable
                                DropdownMenuItem(
                                    text = {
                                        Text("${cita.fecha} ${cita.hora} (P: ${cita.id_usuario.take(4)}...)")
                                    },
                                    onClick = {
                                        idCitaSeleccionada = cita.id_cita
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Campos Tratamiento
                OutlinedTextField(
                    value = descripcion.value,
                    onValueChange = {descripcion.value = it},
                    label = { Text("Descripcion del Tratamiento")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = duracion.value,
                    onValueChange = {duracion.value = it.filter { char -> char.isDigit() }},
                    label = { Text("Duracion Días")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = indicaciones.value,
                    onValueChange = {indicaciones.value = it },
                    label = { Text("Indicaciones")},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(
                        tratamientoInicial.copy(
                                Id_cita = idCitaSeleccionada,
                                descripcion = descripcion.value,
                                duracionDias = duracion.value.toIntOrNull() ?: 0,
                                indicaciones = indicaciones.value
                        )
                    )
                    onDismiss()
                },
                enabled = idCitaSeleccionada.isNotEmpty() && descripcion.value.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}