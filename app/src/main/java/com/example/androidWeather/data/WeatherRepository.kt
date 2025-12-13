package com.example.androidWeather.data

import com.example.androidWeather.Weatherapi
import com.example.androidWeather.Wunderground
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData

/**
 * Repository layer aggregating weather data sources.
 * Simple, framework-free for now; can be swapped to DI later.
 */
class WeatherRepository(
    private val weatherapi: Weatherapi = Weatherapi(),
    private val wunderground: Wunderground = Wunderground(),
) {
    val weatherapiIntervalMinutes: Int get() = weatherapi.intervalInMinutes
    val wundergroundIntervalMinutes: Int get() = wunderground.intervalInMinutes

    suspend fun fetchWeatherapi(): WeatherapiForecast? = weatherapi.fetch()

    suspend fun fetchWunderground(): WundergroundData? = wunderground.fetch()
}
