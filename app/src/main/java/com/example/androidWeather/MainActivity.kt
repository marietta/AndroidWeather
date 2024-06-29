package com.example.androidWeather


import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.androidWeather.dto.WeatherapiForecast
import com.example.myapplication.dto.WeatherapiCurrent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val currentOrientation = LocalConfiguration.current.orientation
                val data = remember { mutableStateOf<WeatherapiCurrent?>(null) }
                val forecastData = remember { mutableStateOf<WeatherapiForecast?>(null) }

                LaunchedEffect(Unit) {
                    while (true) {
                        fetchData(data)
                        fetchForecastData(forecastData)
                        val lastUpdated = data.value?.current?.lastUpdated
//                        for (item in forecastData.value?.forecast?.forecastday?.get(0)?.hour!!) {
//                            if (lastUpdated == item.time) {
//                                println(item.time)
//                                data.value!!.current?.uv = item.uv
//                                data.value!!.current?.tempC = item.tempC
//                            }
//                        }
                        delay(15 * 60 * 1000)
                    }
                }

                when (currentOrientation) {
                    Configuration.ORIENTATION_PORTRAIT -> data.value?.let { PortraitLayout(it) }
                    Configuration.ORIENTATION_LANDSCAPE -> data.value?.let { LandscapeLayout(it) }
                    else -> data.value?.let { PortraitLayout(it) } // Default to portrait if orientation is unknown
                }

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
            parameter("q", "47.396,19.118")
            parameter("days", 1)
        }

    data.value = response.body()
    client.close()

}

suspend fun fetchData(data: MutableState<WeatherapiCurrent?>) {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
            // this: DefaultRequestBuilder
        }
    }

    val response: HttpResponse =
        client.get("https://api.weatherapi.com/v1/current.json?q=Budapest") {
            parameter("key", "1c0a7e979c9c46da9dd112808242606")
        }

    data.value = response.body()
    client.close()
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    var useDarkTheme = true
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
fun LandscapeLayout(data: WeatherapiCurrent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    )
    {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.current?.isDay == 0) {
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
                LayoutTop(data = data)
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    "Last updated: ${data.current?.lastUpdated}",
                    fontSize = 12.sp,
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                LayoutBottom(data = data)
                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
    }

}


@Composable
fun PortraitLayout(data: WeatherapiCurrent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            Spacer(modifier = Modifier.weight(0.1f))
            LayoutTop(data = data)
            Spacer(modifier = Modifier.weight(0.1f))
            LayoutBottom(data)
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                "Last updated: ${data.current?.lastUpdated}",
                fontSize = 12.sp,
            )

        }
    }
}

@Composable
fun LayoutBottom(data: WeatherapiCurrent) {
    val iconUrl = "https:${data.current?.condition?.icon}".replace("64x64", "128x128")
    AsyncImage(
        model = iconUrl,
        contentDescription = "Weather Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
    )
    if (data.current?.isDay == 1) {
        Text(
            "UV ${data.current?.uv?.roundToInt()}",
            fontSize = 48.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun LayoutTop(data: WeatherapiCurrent) {
    val intPart = data.current?.tempC?.toInt()
    val fractionalPart = (intPart?.let { data.current?.tempC?.minus(it) })?.times(10)?.toInt()

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${intPart} ",
            fontSize = 142.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light
        )
        Text(
            ".${fractionalPart} °C",
            fontSize = 48.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light,
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${data.current?.condition?.text}",
            fontSize = 48.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
