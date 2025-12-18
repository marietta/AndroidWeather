package com.example.androidWeather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidWeather.data.WeatherRepository
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WeatherUiState(
    val weatherapi: WeatherapiForecast? = null,
    val wunderground: WundergroundData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState(isLoading = true))
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        // Weatherapi loop
        viewModelScope.launch {
            while (isActive) {
                fetchWeatherapiOnce()
                val interval = repository.weatherapiIntervalMinutes.coerceAtLeast(1)
                delay(interval * 60 * 1000L)
            }
        }
        // Wunderground loop
        viewModelScope.launch {
            while (isActive) {
                fetchWundergroundOnce()
                val interval = repository.wundergroundIntervalMinutes.coerceAtLeast(1)
                delay(interval * 60 * 1000L)
            }
        }
    }

    private suspend fun fetchWeatherapiOnce() {
        val result = try {
            withContext(Dispatchers.IO) { repository.fetchWeatherapi() }
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, error = e.message)
            null
        }
        _state.value = _state.value.copy(weatherapi = result, isLoading = false)
    }

    private suspend fun fetchWundergroundOnce() {
        val result = try {
            withContext(Dispatchers.IO) { repository.fetchWunderground() }
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, error = e.message)
            null
        }
        _state.value = _state.value.copy(wunderground = result, isLoading = false)
    }
}
