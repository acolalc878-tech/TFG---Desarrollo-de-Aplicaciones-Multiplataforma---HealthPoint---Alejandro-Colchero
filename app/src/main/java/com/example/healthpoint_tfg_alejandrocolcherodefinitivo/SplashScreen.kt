package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit){
    // Animacion de fade-in / fade-out del logotipo
    val logo = remember {Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fade-in
        logo.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )

        // Mantenemos 1 segundo
        delay(1000)

        //Fade-out
        logo.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )

        // Notificar que el splash terminó
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Ponemos nuestro logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo4),
                contentDescription = "Logo - HealthPoint - Alejandro Colchero",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(logo.value)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "HealthPoint",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(logo.value)
                )
        }
    }
}