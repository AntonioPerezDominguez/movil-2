package com.example.futbolito

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Obstacle(val position: Offset, val size: Size)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocketSoccerScreen() {
    // Verifica si el sensor de acelerómetro está disponible
    if (isAccelerometerSensorAvailable()) {
        // Obtiene los valores del acelerómetro
        val sensorValue by rememberAccelerometerSensorValueAsState()
        val (x, y, z) = sensorValue.value

        // Dimensiones del campo y de las porterías
        val fieldWidth = 1080f
        val fieldHeight = 1920f
        val goalWidth = 300f
        val goalHeight = 100f
        val goalX = (fieldWidth - goalWidth) / 2

        // Crea una lista de obstáculos que representan las porterías
        val goals = listOf(
            Obstacle(Offset(goalX, 0f), Size(goalWidth, goalHeight)),
            Obstacle(Offset(goalX, fieldHeight - goalHeight), Size(goalWidth, goalHeight)),
        )
        
        // Estado para contar los goles en cada portería
        var goalsLeft by remember { mutableStateOf(0) }
        var goalsRight by remember { mutableStateOf(0) }

        // Componente principal que muestra los valores del acelerómetro y el campo
        Scaffold(
            topBar = { CenterAlignedTopAppBar(title = { Text(text = "America vs Cruz Azul") }) }
        )
        { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
            ) {
                // Obtiene las dimensiones del contenedor disponible
                val width = constraints.maxWidth.toFloat()
                val height = constraints.maxHeight.toFloat()

                // Estado para la posición central del círculo
                var center by remember { mutableStateOf(Offset(width / 2, height / 2)) }

                // Obtiene la orientación actual de la pantalla
                val orientation = LocalConfiguration.current.orientation

                // Obtiene el color del contenido actual
                val contentColor = LocalContentColor.current

                // Define el radio del círculo
                val radius = with(LocalDensity.current) { 15.dp.toPx() }

                // Función para verificar si el círculo colisiona con un obstáculo
                fun isCollided(center: Offset, obstacle: Obstacle, radius: Float): Boolean {
                    return center.x + radius > obstacle.position.x && center.x - radius < obstacle.position.x + obstacle.size.width &&
                            center.y + radius > obstacle.position.y && center.y - radius < obstacle.position.y + obstacle.size.height
                }

                // Verifica colisiones y actualiza la posición y el contador de goles
                fun handleCollision() {
                    // Colisión con las porterías
                    if (isCollided(center, goals[0], radius)) {
                        center = Offset(width / 2, height / 2)
                        goalsLeft++
                    }

                    if (isCollided(center, goals[1], radius)) {
                        center = Offset(width / 2, height / 2)
                        goalsRight++
                    }

                }

                handleCollision()

                // Actualiza la posición del círculo basado en los valores del acelerómetro
                center = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Offset(
                        x = (center.x - x).coerceIn(radius, fieldWidth - radius),
                        y = (center.y + y).coerceIn(radius, fieldHeight - radius),
                    )
                } else {
                    Offset(
                        x = (center.x + y).coerceIn(radius, fieldWidth - radius),
                        y = (center.y + x).coerceIn(radius, fieldHeight - radius),
                    )
                }

                // Contenedor para dibujar el campo, porterías y círculo
                Box {
                    // Dibuja el contenido usando Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Dibuja el campo de juego
                        drawRect(color = Color.Green, size = Size(fieldWidth, fieldHeight))

                        // Dibuja las porterías y los obstáculos centrales
                        goals.forEach { obstacle ->
                            drawRect(
                                color = Color.Black,
                                topLeft = obstacle.position,
                                size = obstacle.size
                            )
                        }


                        // Dibuja el círculo
                        drawCircle(
                            color = if (isCollided(center, goals[0], radius) || isCollided(center, goals[1], radius)) Color.Red else contentColor,
                            radius = radius,
                            center = center,
                        )
                    }

                    // Muestra los contadores de goles
                    Column {
                        Text(text = "America: $goalsLeft")
                        Text(text = "Cruz Azul: $goalsRight")
                    }
                }
            }
        }
    } else {
        // Muestra una interfaz alternativa si el sensor no está disponible
        Text(text = "El acelerómetro no está disponible")
    }
}
