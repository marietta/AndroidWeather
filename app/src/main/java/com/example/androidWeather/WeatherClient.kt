package com.example.androidWeather

import android.util.Log
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.V3WxObservations
import com.example.androidWeather.dto.wunderground.WundergroundData
import com.example.androidWeather.network.HttpClients
import io.ktor.client.call.*
import io.ktor.client.request.*

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : ApiResult<Nothing>()
}

interface WeatherApi<T> {
    val lat: Double get() = 47.395
    val lon: Double get() = 19.123
    val intervalInMinutes: Int get() = 10

    suspend fun fetch(): ApiResult<T>
}

class WeatherapiClient : WeatherApi<WeatherapiForecast> {
    private val apiKey = BuildConfig.WEATHERAPI_KEY

    private val url: String
        get() = "https://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$lat,$lon&days=1"

    override suspend fun fetch(): ApiResult<WeatherapiForecast> {
        return try {
            val response = HttpClients.default.get(url)
            if (response.status.value == 200) {
                ApiResult.Success(response.body())
            } else {
                Log.e("WeatherapiClient", "Error response: ${response.status}")
                ApiResult.Error("API Error: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("WeatherapiClient", e.toString())
            ApiResult.Error("Unexpected error", e)
        }
    }
}

class WundergroundClient : WeatherApi<WundergroundData> {
    private val apiKey = BuildConfig.WUNDERGROUND_KEY

    private val baseUrl = "https://api.weather.com/v2/pws/observations/current"
    private val v3Url = "https://api.weather.com/v3/aggcommon/v3-wx-observations-current"

    private fun getPwsUrl(stationId: String) =
        "$baseUrl?apiKey=$apiKey&stationId=$stationId&numericPrecision=decimal&format=json&units=m"

    private val observationsUrl =
        "$v3Url?apiKey=$apiKey&geocodes=$lat,$lon&language=en-US&units=m&format=json"

    override suspend fun fetch(): ApiResult<WundergroundData> {
        return try {
            // Main observation from a station
            val response = HttpClients.default.get(getPwsUrl("IBUDAP576"))
            val baseData: WundergroundData? = if (response.status.value == 200) {
                response.body()
            } else {
                Log.e("WundergroundClient", "PWS error: ${response.status}")
                null
            }

            // Additional observations from V3 API
            val v3Response = HttpClients.default.get(observationsUrl)
            val v3Data: List<V3WxObservations?> = if (v3Response.status.value == 200) {
                val response:List<V3WxObservations?> = v3Response.body()
                if (response.firstOrNull()?.observationsCurrent != null) v3Response.body() else emptyList()
            } else {
                Log.e("WundergroundClient", "V3 error: ${v3Response.status}")
                emptyList()
            }

            if (baseData == null && v3Data.isEmpty()) {
                ApiResult.Error("Failed to fetch Wunderground data")
            } else {
                ApiResult.Success((baseData ?: WundergroundData(emptyList())).copy(observationsCurrent = v3Data))
            }
        } catch (e: Exception) {
            Log.e("WundergroundClient", e.toString())
            ApiResult.Error("Unexpected error", e)
        }
    }
}
