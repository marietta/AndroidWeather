package com.example.androidWeather

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidWeather.ui.WeatherUiState
import com.example.androidWeather.ui.WeatherViewModel
import com.example.androidWeather.ui.components.LandscapeLayout
import com.example.androidWeather.ui.components.PortraitLayout
import com.example.androidWeather.utils.getDrawableResourceId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep the screen on and prepare system bars behavior once
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            MyApp {
                // ViewModel holds and updates weather state
                val vm: WeatherViewModel = viewModel()
                val uiState by vm.state.collectAsState()

                WeatherContent(uiState)
            }
        }

        // Hide system bars outside of composition so it runs only once
        hideSystemBars()
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

@Composable
fun WeatherContent(uiState: WeatherUiState) {
    val orientation = LocalConfiguration.current.orientation
    val frontColor = if (uiState.weatherapi?.current?.isDay == 0) {
        Color.LightGray
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    CompositionLocalProvider(LocalContentColor provides frontColor) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeLayout(uiState, ::getDrawableResourceId)
        } else {
            PortraitLayout(uiState, ::getDrawableResourceId)
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    val useDarkTheme = true
    // Force an absolute black theme for background and surfaces
    val colors = if (useDarkTheme) {
        darkColorScheme(
            background = Color.Black,
            surface = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
        )
    } else {
        lightColorScheme()
    }
    MaterialTheme(
        colorScheme = colors,
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


