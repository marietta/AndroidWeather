package com.example.androidWeather

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androidWeather.dto.openMeteo.OpenMeteoForecast
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val orientation = LocalConfiguration.current.orientation
                val handler = remember { Handler(Looper.getMainLooper()) }
                val weatherapiForecastData = periodicFetch(handler, Weatherapi())
                val openMeteoForecastData = periodicFetch(handler, OpenMeteo())
//                val accuweatherApiData = periodicFetch(handler, Accuweather())
                val wunderData = periodicFetch(handler, Wunderground())

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) LandscapeLayout(
                    weatherapiForecastData, openMeteoForecastData, wunderData
                )
                else PortraitLayout(
                    weatherapiForecastData, openMeteoForecastData, wunderData
                )

                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
                windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
                // Configure the behavior of the hidden system bars.
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    val useDarkTheme = true
    MaterialTheme(
        colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
        content = {
            Surface(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) {
                content()
            }
        })
}

@Composable
fun LandscapeLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>,
    data4: MutableState<WundergroundData?>,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.value?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LayoutTop(
                    weatherapiData = data.value,
                    openMeteoData = data2.value,
                    wunderData = data4.value,
                )
                if (data4.value != null) {
                    Text(
                        "Last updated: ${data4.value?.observations?.firstOrNull()?.obsTimeLocal}",
                        fontSize = 12.sp,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LayoutBottom(weatherapiData = data.value, wunderData = data4.value)
            }
        }
    }
}


@Composable
fun PortraitLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>,
    data4: MutableState<WundergroundData?>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.value?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            LayoutTop(
                weatherapiData = data.value,
                openMeteoData = data2.value,
                wunderData = data4.value,
            )
            LayoutBottom(data.value, data4.value)
            if (data4.value != null) {
                Text(
                    "Last updated: ${data4.value?.observations?.firstOrNull()?.obsTimeLocal}",
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun LayoutTop(
    weatherapiData: WeatherapiForecast?,
    openMeteoData: OpenMeteoForecast?,
    wunderData: WundergroundData?,
) {

    val temp = wunderData?.observations?.firstOrNull()?.metric?.temp
    val intPart = temp?.toInt()

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (wunderData == null) {
            Text(
                text = "Loading...",
                fontSize = 48.sp,
            )
        } else {
            Text(
                "$intPart ",
                fontSize = 142.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
            )
            Text(
                "°C",
                fontSize = 48.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
            )
        }
    }

    if (wunderData != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.wxPhraseLong.toString(),
                fontSize = 48.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun LayoutBottom(
    weatherapiData: WeatherapiForecast?,
    wunderData: WundergroundData?,
) {
    if (wunderData != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    pressureSensorScreen(weatherapiData?.current?.isDay)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val humid = wunderData.observations.firstOrNull()?.humidity?.toInt()
                    Text(text = "${humid.toString()} %", fontSize = 18.sp)
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Humid Icon",
                        modifier = Modifier.size(56.dp).padding(12.dp)
                    )
                    val humidText = when {
                        humid!! < 40 -> "Dry"
                        humid < 60 -> "Good"
                        humid < 80 -> "Humid" // patchy rain
                        else -> "Wet"
                    }
                    Text(text = humidText, fontSize = 18.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val dewpt = wunderData.observations.firstOrNull()?.metric?.dewpt?.toInt()
                    Text(text = "Dew point", fontSize = 18.sp)
                    Icon(
                        painter = painterResource(id = R.drawable.dew_point_24dp),
                        contentDescription = "Humid Icon",
                        modifier = Modifier.size(56.dp).padding(12.dp)
                    )
                    Text(text = "${dewpt.toString()} °C", fontSize = 18.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val windSpeed = wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.windSpeed
                    Text(text = "$windSpeed km/h")
                    Icon(
                        imageVector = Icons.Outlined.Air,
                        contentDescription = "Air Icon",
                        modifier = Modifier.size(64.dp).padding(12.dp)
                    )
                    val windText = when {
                        windSpeed == 0 -> "Still"
                        windSpeed!! < 10 -> "Light breeze" // rain
                        windSpeed < 20 -> "Windy" // patchy rain
                        else -> "Strong Wind" // clear
                    }
                    Text(text = windText, fontSize = 18.sp)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iconCode = wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.iconCode
            val dayOrNight =
                wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.dayOrNight?.lowercase()

            Image(
                painter = painterResource(id = getDrawableResourceId(iconCode, dayOrNight)),
                contentDescription = "Weather Image",
                modifier = Modifier
                    .height(160.dp)
            )
        }

        if (wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.dayOrNight == "D") {
            var fontWeight = FontWeight.Light
            var color = Color.LightGray
            var fontSize = 36.sp
            val uv = wunderData.observations.firstOrNull()?.uv?.toInt()
            if (uv != null) {
                if (uv >= 3) {
                    fontWeight = FontWeight.Normal
                    color = Color(255, 200, 0)
                    fontSize = 54.sp
                }
                if (uv >= 6) {
                    fontWeight = FontWeight.Bold
                    color = Color(252, 174, 0)
                }
                if (uv >= 8) {
                    color = Color(209, 57, 74)
                }
                Text(
                    "UV $uv",
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = color,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }

    }
}


fun getDrawableResourceId(iconCode: Int? = 30, dayOrNight: String? = "d"): Int {
    val resourceName = "im_${dayOrNight}_$iconCode"
    return try {
        // Construct the resource name based on the icon code
        // Get the resource ID dynamically
        val resId = R.drawable::class.java.getField(resourceName).getInt(null)
        resId
    } catch (_: Exception) {
        Log.d("Wunderground wx", "Missing icon: $resourceName")
        R.drawable.im_d_28 // Fallback if not found
    }
}
