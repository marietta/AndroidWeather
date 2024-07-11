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
import androidx.compose.runtime.MutableState
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

                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    LandscapeLayout(
                        weatherapiForecastData,
                        openMeteoForecastData,
                        accuweatherApiData
                    )
                else PortraitLayout(
                    weatherapiForecastData,
                    openMeteoForecastData,
                    accuweatherApiData
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
        }
    )
}

@Composable
fun LandscapeLayout(
    data: MutableState<WeatherapiForecast?>,
    data2: MutableState<OpenMeteoForecast?>,
    data3: MutableState<AccuweatherApiItem?>,
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
                LayoutTop(data = data.value, data2 = data2.value, data3 = data3.value)
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
    data2: MutableState<OpenMeteoForecast?>,
    data3: MutableState<AccuweatherApiItem?>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (data.value?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            Spacer(modifier = Modifier.weight(0.1f))
            LayoutTop(data = data.value, data2 = data2.value, data3 = data3.value)
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
fun LayoutTop(
    data: WeatherapiForecast?,
    data2: OpenMeteoForecast?,
    data3: AccuweatherApiItem?
) {
    val tempC = data?.current?.tempC
    val intPart = data?.current?.tempC?.toInt()
    val fractionalPart =
        (intPart?.let { tempC?.minus(it) })?.times(10)?.toInt()

    Row {
        Text(text = "accuw ${data3?.temperature?.metric?.value} ${data3?.weatherText}")
    }
    Row {
        Text(text = "open-meteo ${data2?.current?.temperature2m} ")
    }
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
            "${data?.current?.condition?.text}",
            fontSize = 48.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(top = 24.dp)
        )

    }
}

@Composable
fun LayoutBottom(
    data: WeatherapiForecast?,
    data2: OpenMeteoForecast?,
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