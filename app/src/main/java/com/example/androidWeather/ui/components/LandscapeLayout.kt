package com.example.androidWeather.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidWeather.ui.WeatherUiState

@Composable
fun LandscapeLayout(
    state: WeatherUiState,
    getDrawableResourceId: (Int?) -> Int
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainWeatherInfo(state)
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PressureSensorDisplay()
            LayoutBottom(wunderData = state.wunderground, getDrawableResourceId = getDrawableResourceId)
        }
    }
}
