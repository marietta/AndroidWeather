package com.example.androidWeather.ui

import com.example.androidWeather.ApiResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidWeather.data.WeatherRepository
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        startLoops()
    }

    private fun startLoops() {
        viewModelScope.launch {
            launch {
                while (isActive) {
                    fetchWeatherapiOnce()
                    delay(repository.weatherapiIntervalMinutes.coerceAtLeast(1) * 60 * 1000L)
                }
            }
            launch {
                while (isActive) {
                    fetchWundergroundOnce()
                    delay(repository.wundergroundIntervalMinutes.coerceAtLeast(1) * 60 * 1000L)
                }
            }
        }
    }

    private suspend fun fetchWeatherapiOnce() {
        val result = repository.fetchWeatherapi()
        _state.update { currentState ->
            when (result) {
                is ApiResult.Success -> {
                    if (result.data != null) {
                        currentState.copy(
                            weatherapi = result.data,
                            isLoading = false,
                            error = null
                        )
                    } else {
                        currentState.copy(isLoading = false)
                    }
                }
                is ApiResult.Error -> currentState.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    private suspend fun fetchWundergroundOnce() {
        val result = repository.fetchWunderground()
        _state.update { currentState ->
            when (result) {
                is ApiResult.Success -> {
                    if (result.data != null) {
                        currentState.copy(
                            wunderground = result.data,
                            isLoading = false,
                            error = null
                        )
                    } else {
                        currentState.copy(isLoading = false)
                    }
                }
                is ApiResult.Error -> currentState.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }
}
