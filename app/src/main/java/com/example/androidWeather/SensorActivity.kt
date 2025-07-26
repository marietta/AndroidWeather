package com.example.androidWeather

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PressureSensorScreen1(isDay: Int?) {
    val context = LocalContext.current
    var pressureValue by remember { mutableStateOf<Float?>(null) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_PRESSURE)
                    pressureValue = event.values[0]
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (sensor != null)
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        val pressure = (pressureValue ?: 0f).toInt()
        Text(text = "$pressure hPa")

        Icon(
            painter = painterResource(id = R.drawable.cyclone_48px),
            contentDescription = "Pressure Icon",
            modifier = Modifier.size(56.dp).padding(12.dp)
        )

        val ptext = when {
            pressure < 990 -> "storm and chaos"
            pressure < 996 -> "wet and windy bleh"
            pressure < 1001 -> "clouds and sun"
            pressure < 1030 -> "calm and clear"
            else -> "danger of drought"
        }
        Text(text = ptext)
    }
}




