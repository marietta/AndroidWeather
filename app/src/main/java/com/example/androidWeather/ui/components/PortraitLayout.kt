package com.example.androidWeather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidWeather.ui.WeatherUiState

@Composable
fun PortraitLayout(
    state: WeatherUiState,
    getDrawableResourceId: (Int?) -> Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainWeatherInfo(state)

        PressureSensorDisplay()

        LayoutBottom(state.wunderground, getDrawableResourceId)
    }
}
