package com.example.androidWeather

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import com.example.androidWeather.dto.accuweather.AccuweatherApiItem
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
                val accuweatherApiData = periodicFetch(handler, Accuweather())
                val wunderData = periodicFetch(handler, Wunderground())

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) LandscapeLayout(
                    weatherapiForecastData, openMeteoForecastData, accuweatherApiData, wunderData
                )
                else PortraitLayout(
                    weatherapiForecastData, openMeteoForecastData, accuweatherApiData, wunderData
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
                modifier = Modifier.fillMaxSize(),
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
    data3: MutableState<AccuweatherApiItem?>,
    data4: MutableState<WundergroundData?>,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
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
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LayoutTop(
                    weatherapiData = data.value,
                    openMeteoData = data2.value,
                    accuweatherData = data3.value,
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
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LayoutBottom(data = data.value, data2 = data2.value, data4.value)
            }
        }
    }
}


@Composable
fun PortraitLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>,
    data3: MutableState<AccuweatherApiItem?>,
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
                accuweatherData = data3.value,
                wunderData = data4.value,
            )
            LayoutBottom(data.value, data2.value, data4.value)
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
    accuweatherData: AccuweatherApiItem?,
    wunderData: WundergroundData?,
) {
    val temp = wunderData?.observations?.firstOrNull()?.metric?.temp
    val intPart = temp?.toInt()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            if (weatherapiData != null) {
                Text(text = "weatherapi: ${weatherapiData.current?.tempC} ")
            } else Text(text="")
            if (openMeteoData != null) {
                Text(text = "open-meteo: ${openMeteoData.current?.temperature2m} ")
            } else Text(text="")
        }
        Row {
            Text(text = "${wunderData?.observations?.firstOrNull()?.metric?.pressure?.toInt()} hPa")
        }
    }

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

    if (weatherapiData != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "${weatherapiData.current?.condition?.text}",
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
    data: WeatherapiForecast?,
    data2: OpenMeteoForecast?,
    data4: WundergroundData?,
) {
    val iconUrl = "https:${data?.current?.condition?.icon}".replace("64x64", "128x128")
    AsyncImage(
        model = iconUrl,
        contentDescription = "Weather Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
    )
    if (data?.current?.isDay == 1) {
        var fontWeight = FontWeight.Light
        var color = Color.LightGray
        var fontSize = 36.sp
        val uv = data4?.observations?.firstOrNull()?.uv?.toInt()
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