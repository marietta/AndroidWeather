package com.example.androidWeather

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SensorManagerSingleton {
    private var sensorManager: SensorManager? = null
    private var pressureSensor: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null

    fun initialize(context: Context) {
        if (sensorManager == null) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
        }
    }

    fun registerListener(listener: SensorEventListener) {
        if (pressureSensor != null && sensorEventListener == null) {
            sensorEventListener = listener
            sensorManager?.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterListener() {
        sensorEventListener?.let {
            sensorManager?.unregisterListener(it)
            sensorEventListener = null // Clear the reference after unregistering
        }
    }
}

@Composable
fun pressureSensorScreen(isDay: Int?) {
    val context = LocalContext.current
    var pressureValue by remember { mutableStateOf(0f) }

    // Create a SensorEventListener to handle sensor changes
    val sensorEventListener = rememberUpdatedState(object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PRESSURE) {
                pressureValue = event.values[0] // Get the pressure value in hPa
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    })

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // Initialize the singleton with context
        SensorManagerSingleton.initialize(context)

        // Register the listener
        SensorManagerSingleton.registerListener(sensorEventListener.value)

        coroutineScope.launch {
            while (true) {
                delay(60000) // Wait for 1 minute (60,000 milliseconds)
                // Optionally, you can re-register or perform any other action here if needed.
                // For example, you might want to log or process the pressure value.
            }
        }

        onDispose {
            // Unregister the listener when the composable is disposed
            SensorManagerSingleton.unregisterListener()
        }
    }

    // UI to display the pressure value
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val pressure = pressureValue.toInt()
        val iconId = when {
            pressure < 992 -> 308 // rain
            pressure < 996 -> 293 // patchy rain
            pressure < 1001 -> 119 // cloudy
            pressure < 1004 -> 116 // partly cloudy
            else -> 113 // clear
        }
        Text(text = "$pressure hPa")
        var day = "day"
        if (isDay != 1) {
            day = "night"
        }

        AsyncImage(
            model = "https://cdn.weatherapi.com/weather/64x64/$day/$iconId.png",
            contentDescription = "Weather Image",
            modifier = Modifier.height(64.dp)
        )
    }
}

