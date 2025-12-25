package com.example.androidWeather.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidWeather.R

import com.example.androidWeather.ui.WeatherUiState

@Composable
fun WeatherError(message: String) {
    Text(
        text = stringResource(R.string.error_format, message),
        color = Color.Red,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun NoWeatherData() {
    Text(
        text = stringResource(R.string.no_weather_data),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun LastUpdated(time: String) {
    Text(
        text = stringResource(R.string.last_updated_format, time),
        fontSize = 12.sp,
    )
}

@Composable
fun MainWeatherInfo(state: WeatherUiState) {
    LayoutTop(
        wunderData = state.wunderground,
        isLoading = state.isLoading,
    )
    if (state.error != null && state.wunderground == null) {
        WeatherError(message = state.error)
    }
    if (!state.isLoading && state.wunderground == null && state.error == null) {
        NoWeatherData()
    }
    state.wunderground?.observations?.firstOrNull()?.obsTimeLocal?.let { time ->
        Row(modifier = Modifier.padding(top = 8.dp)) {
            LastUpdated(time)
        }
    }
}
