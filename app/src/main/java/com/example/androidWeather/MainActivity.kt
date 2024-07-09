package com.example.androidWeather


import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import com.example.androidWeather.dto.openMeteo.OpenMeteoForecast
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val currentOrientation = LocalConfiguration.current.orientation
                val forecastData = remember { mutableStateOf<WeatherapiForecast?>(null) }
                val openMeteoForecastData = remember { mutableStateOf<OpenMeteoForecast?>(null) }

                LaunchedEffect(Unit) {
                    fetchOpenMeteoForecastData(openMeteoForecastData)
                    fetchForecastData(forecastData)
                }

                startRepeatingJob(forecastData, openMeteoForecastData)

                when (currentOrientation) {
                    Configuration.ORIENTATION_PORTRAIT -> PortraitLayout(
                        forecastData,
                        openMeteoForecastData
                    )

                    Configuration.ORIENTATION_LANDSCAPE -> LandscapeLayout(
                        forecastData,
                        openMeteoForecastData
                    )

                    else -> {
                        PortraitLayout(forecastData, openMeteoForecastData)
                    }
                } // Default to portrait if orientation is unknown

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

private fun startRepeatingJob(
    forecastData: MutableState<WeatherapiForecast?>,
    openMeteoForecastData: MutableState<OpenMeteoForecast?>,
    timeInterval: Long = 15 * 60 * 1000L
): Job {
    return CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            delay(timeInterval)
            fetchOpenMeteoForecastData(openMeteoForecastData)
            fetchForecastData(forecastData)
        }
    }
}

suspend fun fetchForecastData(data: MutableState<WeatherapiForecast?>) {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
        }
    }

    val response: HttpResponse =
        client.get("https://api.weatherapi.com/v1/forecast.json") {
            parameter("key", "1c0a7e979c9c46da9dd112808242606")
            parameter("q", "47.395,19.123")
            parameter("days", 1)
        }
    data.value = response.body()
    client.close()
}

suspend fun fetchOpenMeteoForecastData(data: MutableState<OpenMeteoForecast?>) {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
        }
    }

    val response: HttpResponse =
        client.get("https://api.open-meteo.com/v1/forecast?latitude=47.4&longitude=19.12&current=temperature_2m&minutely_15=temperature_2m&hourly=temperature_2m,uv_index&timezone=Europe%2FBerlin&forecast_days=1&forecast_hours=1&forecast_minutely_15=4") {
        }
    data.value = response.body()
    client.close()
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
        }
    )


}

@Composable
fun LandscapeLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    )
    {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.value?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                LayoutTop(data = data, data2 = data2.value)
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    "Last updated: ${data.value?.current?.lastUpdated}",
                    fontSize = 12.sp,
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(0.1f))
                LayoutBottom(data = data.value, data2 = data2.value)
                Spacer(modifier = Modifier.weight(0.2f))
            }
        }
    }

}


@Composable
fun PortraitLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.value?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            Spacer(modifier = Modifier.weight(0.1f))
            LayoutTop(data = data, data2 = data2.value)
            LayoutBottom(data.value, data2.value)
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                "Last updated: ${data.value?.current?.lastUpdated}",
                fontSize = 12.sp,
            )

        }
    }
}

@Composable
fun LayoutBottom(data: WeatherapiForecast?, data2: OpenMeteoForecast?) {
    val iconUrl = "https:${data?.current?.condition?.icon}".replace("64x64", "128x128")
    AsyncImage(
        model = iconUrl,
        contentDescription = "Weather Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
    )
    if (data != null) {
        if (data.current?.isDay == 1) {
            if (data2 != null) {
                var fontWeight = FontWeight.Light
                var color = Color.LightGray
                var fontSize = 36.sp
                val uv = data2.hourly?.uvIndex?.first()?.toDouble()
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
    }
}

@Composable
fun LayoutTop(data: MutableState<WeatherapiForecast?>, data2: OpenMeteoForecast?) {
//    val intPart = data2?.current?.temperature2m?.toInt()
    val tempC = data.value?.current?.tempC
    val intPart = data.value?.current?.tempC?.toInt()
    val fractionalPart =
        (intPart?.let { tempC?.minus(it) })?.times(10)?.toInt()

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
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
        Spacer(modifier = Modifier.weight(0.1f))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${data.value?.current?.condition?.text}",
            fontSize = 48.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
