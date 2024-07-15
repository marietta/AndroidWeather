package com.example.androidWeather

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) LandscapeLayout(
                    weatherapiForecastData, openMeteoForecastData, accuweatherApiData
                )
                else PortraitLayout(
                    weatherapiForecastData, openMeteoForecastData, accuweatherApiData
                )

                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
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
    MaterialTheme(colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(), content = {
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
                modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LayoutTop(weatherapiData = data.value, openMeteoData = data2.value, accuweatherData = data3.value)
                if (data.value != null) {
                    Text(
                        "Last updated: ${data.value?.current?.lastUpdated}",
                        fontSize = 12.sp,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LayoutBottom(data = data.value, data2 = data2.value)
            }
        }
    }
}


@Composable
fun PortraitLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>,
    data3: MutableState<AccuweatherApiItem?>,
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
            LayoutTop(weatherapiData = data.value, openMeteoData = data2.value, accuweatherData = data3.value)
            LayoutBottom(data.value, data2.value)
            if (data.value != null) {
                Text(
                    "Last updated: ${data.value?.current?.lastUpdated}",
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun LayoutTop(
    weatherapiData: WeatherapiForecast?, openMeteoData: OpenMeteoForecast?, accuweatherData: AccuweatherApiItem?
) {
    val temp = openMeteoData?.minutely15?.temperature2m?.first()
    val intPart = temp?.toInt()
    val fractionalPart = (intPart?.let { temp.minus(it) })?.times(10)?.toInt()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (accuweatherData != null) {
            Row {
                Text(
                    text = "accuw ${accuweatherData.temperature?.metric?.value} ${accuweatherData.weatherText}"
                )
            }
        }
        if (weatherapiData != null) {
            Row {
                Text(text = "weatherapi current: ${weatherapiData.current?.tempC} ")
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (weatherapiData == null) {
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
                ".${fractionalPart} °C",
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
) {
    val iconUrl = "https:${data?.current?.condition?.icon}".replace("64x64", "128x128")
    AsyncImage(
        model = iconUrl, contentDescription = "Weather Image", modifier = Modifier.fillMaxWidth().height(256.dp)
    )
    if (data?.current?.isDay == 1) {
        var fontWeight = FontWeight.Light
        var color = Color.LightGray
        var fontSize = 36.sp
        val uv = data2?.hourly?.uvIndex?.first()?.toDouble()
        if (uv != null) {
            if (uv > 2) {
                fontWeight = FontWeight.Normal
                fontSize = 54.sp
            }
            if (uv > 5) {
                fontWeight = FontWeight.Bold
                color = Color(252, 174, 0)
            }
            if (uv > 7) {
                color = Color(252, 199, 60)
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