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
    suspend fun fetch(lat: Double, lon: Double): ApiResult<T>
}

class WeatherapiClient : WeatherApi<WeatherapiForecast> {
    private val apiKey = BuildConfig.WEATHERAPI_KEY

    private fun getUrl(lat: Double, lon: Double) =
        "https://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$lat,$lon&days=1"

    override suspend fun fetch(lat: Double, lon: Double): ApiResult<WeatherapiForecast> {
        return try {
            val response = HttpClients.default.get(getUrl(lat, lon))
            if (response.status.value == 200) {
                ApiResult.Success(response.body())
            } else {
                Log.e("WeatherapiClient", "Error response: ${response.status}")
                ApiResult.Error("API Error: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("WeatherapiClient", "Fetch failed: ${e.message}")
            ApiResult.Error("Unexpected error", e)
        }
    }
}

class WundergroundClient : WeatherApi<WundergroundData> {
    private val apiKey = BuildConfig.WUNDERGROUND_KEY

    private val pwsBaseUrl = "https://api.weather.com/v2/pws/observations/current"
    private val v3BaseUrl = "https://api.weather.com/v3/aggcommon/v3-wx-observations-current"

    private fun getPwsUrl(stationId: String) =
        "$pwsBaseUrl?apiKey=$apiKey&stationId=$stationId&numericPrecision=decimal&format=json&units=m"

    private fun getV3Url(lat: Double, lon: Double) =
        "$v3BaseUrl?apiKey=$apiKey&geocodes=$lat,$lon&language=en-US&units=m&format=json"

    suspend fun fetch(lat: Double, lon: Double, stationId: String): ApiResult<WundergroundData> {
        return try {
            // Main observation from a station
            val pwsResponse = HttpClients.default.get(getPwsUrl(stationId))
            val baseData: WundergroundData? = if (pwsResponse.status.value == 200) {
                pwsResponse.body()
            } else {
                Log.e("WundergroundClient", "PWS error: ${pwsResponse.status}")
                null
            }

            // Additional observations from V3 API
            val v3Response = HttpClients.default.get(getV3Url(lat, lon))
            val v3Data: List<V3WxObservations?> = if (v3Response.status.value == 200) {
                val body: List<V3WxObservations?> = v3Response.body()
                if (body.firstOrNull()?.observationsCurrent != null) body else emptyList()
            } else {
                Log.e("WundergroundClient", "V3 error: ${v3Response.status}")
                emptyList()
            }

            if (baseData == null && v3Data.isEmpty()) {
                ApiResult.Error("Failed to fetch Wunderground data")
            } else {
                val finalData = (baseData ?: WundergroundData(emptyList())).copy(observationsCurrent = v3Data)
                ApiResult.Success(finalData)
            }
        } catch (e: Exception) {
            Log.e("WundergroundClient", "Fetch failed: ${e.message}")
            ApiResult.Error("Unexpected error", e)
        }
    }

    override suspend fun fetch(lat: Double, lon: Double): ApiResult<WundergroundData> {
        return fetch(lat, lon, "IBUDAP576") // Default station
    }
}
