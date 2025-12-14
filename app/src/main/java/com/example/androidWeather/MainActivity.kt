package com.example.androidWeather

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidWeather.dto.wunderground.WundergroundData
import com.example.androidWeather.ui.WeatherUiState
import com.example.androidWeather.ui.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep the screen on and prepare system bars behavior once
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            MyApp {
                val orientation = LocalConfiguration.current.orientation

                // ViewModel holds and updates weather state
                val vm: WeatherViewModel = viewModel()
                val uiState by vm.state.collectAsState()

                // Show layouts; the composables observe the MutableState values and will recompose
                // when new data arrives.
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) LandscapeLayout(uiState)
                else PortraitLayout(uiState)
            }
        }

        // Hide system bars outside of composition so it runs only once
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    val useDarkTheme = true
    MaterialTheme(
        colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                color = Color.Black,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) {
                content()
            }
        })
}

@Composable
fun LandscapeLayout(
    state: WeatherUiState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (state.weatherapi?.current?.isDay == 0) {
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
                    wunderData = state.wunderground,
                    isLoading = state.isLoading,
                )
                if (state.wunderground != null) {
                    Text(
                        "Last updated: ${state.wunderground.observations.firstOrNull()?.obsTimeLocal}",
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
                LayoutBottom(wunderData = state.wunderground)
            }
        }
    }
}


@Composable
fun PortraitLayout(
    state: WeatherUiState,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var frontColor = MaterialTheme.colorScheme.onBackground
        if (state.weatherapi?.current?.isDay == 0) {
            frontColor = Color.LightGray
        }
        CompositionLocalProvider(LocalContentColor provides frontColor) {
            LayoutTop(
                wunderData = state.wunderground,
                isLoading = state.isLoading,
            )
            LayoutBottom(state.wunderground)
            if (state.wunderground != null) {
                Text(
                    "Last updated: ${state.wunderground.observations.firstOrNull()?.obsTimeLocal}",
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun LayoutTop(
    wunderData: WundergroundData?,
    isLoading: Boolean = false,
) {


    if (isLoading) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "Loading...",
                fontSize = 48.sp,
            )
        }
    } else if (wunderData != null) {
        val intPart = wunderData.observations.firstOrNull()?.metric?.temp?.toInt()
        if (intPart != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
    wunderData: WundergroundData?,
) {
    if (wunderData != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PressureSensorDisplay()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val humid = wunderData.observations.firstOrNull()?.humidity?.toInt()
                Text(text = "${humid ?: "-"} %", fontSize = 18.sp) // Fallback for humid
                Icon(
                    imageVector = Icons.Outlined.WaterDrop,
                    contentDescription = "Humid Icon",
                    modifier = Modifier
                        .size(56.dp)
                        .padding(6.dp)
                )
                val humidText = when {
                    humid == null -> "-" // Handle null humid
                    humid < 40 -> "Dry"
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
                    modifier = Modifier
                        .size(56.dp)
                        .padding(6.dp)
                )
                Text(text = "${dewpt ?: "-"} °C", fontSize = 18.sp) // Fallback for dewpt
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val windSpeed = wunderData.observationsCurrent.firstOrNull()?.observationsCurrent?.windSpeed
                Text(text = "${windSpeed ?: "-"} km/h") // Fallback for windSpeed
                Icon(
                    imageVector = Icons.Outlined.Air,
                    contentDescription = "Air Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(6.dp)
                )
                val windText = when {
                    windSpeed == null -> "-" // Handle null windSpeed
                    windSpeed == 0 -> "Still"
                    windSpeed < 10 -> "Light breeze" // rain
                    windSpeed < 20 -> "Windy" // patchy rain
                    else -> "Strong Wind" // clear
                }
                Text(text = windText, fontSize = 18.sp)
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
                    .height(100.dp)
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
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                // Fallback for UV index if null
                Text(
                    "UV -",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

    } else {
        // Fallback UI when wunderData is entirely null
        Text(
            text = "",
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
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

