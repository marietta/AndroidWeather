package com.example.androidWeather.data

import com.example.androidWeather.ApiResult
import com.example.androidWeather.WeatherapiClient
import com.example.androidWeather.WundergroundClient
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository layer aggregating weather data sources.
 * Simple, framework-free for now; can be swapped to DI later.
 */
class WeatherRepository(
    private val weatherapi: WeatherapiClient = WeatherapiClient(),
    private val wunderground: WundergroundClient = WundergroundClient(),
) {
    val lat = 47.395
    val lon = 19.123
    val stationId = "IBUDAP576"

    val weatherapiIntervalMinutes: Int = 10
    val wundergroundIntervalMinutes: Int = 10

    suspend fun fetchWeatherapi(): ApiResult<WeatherapiForecast> = withContext(Dispatchers.IO) {
        weatherapi.fetch(lat, lon)
    }

    suspend fun fetchWunderground(): ApiResult<WundergroundData> = withContext(Dispatchers.IO) {
        wunderground.fetch(lat, lon, stationId)
    }
}
