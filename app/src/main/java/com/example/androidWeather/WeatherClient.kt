package com.example.androidWeather

import android.util.Log
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData
import com.example.androidWeather.network.HttpClients
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.get
import kotlinx.coroutines.delay

interface Api<T> {
    val lat: Double
        get() = 47.395
    val lon: Double
        get() = 19.123
    val url: String
    val intervalInMinutes: Int
        get() = 10
    suspend fun fetch(): T? // Modified to return T?
}

class Weatherapi : Api<WeatherapiForecast?> {
    // Removed: override val data = mutableStateOf<WeatherapiForecast?>(null)
    private val apiKey = BuildConfig.WEATHERAPI_KEY

    override val url: String
        get() = "https://api.weatherapi.com/v1/forecast.json?key=${apiKey}" +
                "&q=${lat},${lon}&days=1"

    override suspend fun fetch(): WeatherapiForecast? { // Modified return type
        return try {
            val response = HttpClients.default.get(url)
            Log.d("Weatherapi", response.status.toString())
            if (response.status.value == 200) {
                response.body()
            } else null
        } catch (e: ConnectTimeoutException) {
            delay(2000L)
            null
        } catch (e: Exception) {
            Log.e("Weatherapi", "unexpected error", e)
            null
        }
    }
}

class Wunderground : Api<WundergroundData?> {
    // Removed: override val data = mutableStateOf<WundergroundData?>(null)
    private val apiKey = BuildConfig.WUNDERGROUND_KEY

    override val url: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=${apiKey}&stationId=IBUDAP507&numericPrecision=decimal&format=json&units=m"
    val url2: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=${apiKey}&stationId=IBUDAP576&numericPrecision=decimal&format=json&units=m"

    val observations: String
        get() = "https://api.weather.com/v3/aggcommon/v3-wx-observations-current?apiKey=${apiKey}&geocodes=${lat},${lon}&language=en-US&units=m&format=json"

    override suspend fun fetch(): WundergroundData? { // Modified return type
        var wunderData: WundergroundData? = null
//        val response1 = HttpClients.default.get(url)
//        if (response1.status.value == 200) data.value = response1.body()
//        else Log.d("Wunderground", "IBUDAP507 "+response1.status.toString())

        val response2 = HttpClients.default.get(url2)
        if (response2.status.value == 200) {
            val parsed: WundergroundData = response2.body()
            wunderData = parsed
            val uvIndex = parsed.observations.firstOrNull()?.uv
            if (uvIndex != null) {
                wunderData.observations.firstOrNull()?.uv = uvIndex
            } else {
                Log.d("Wunderground", " uv " + response2.status.toString())
            }
        }

        val response3 = HttpClients.default.get(observations)
        if (response3.status.value == 200) {
            wunderData?.observationsCurrent = response3.body()
            Log.d("Wunderground", response3.status.toString())
        } else {
            Log.d("Wunderground wx", response2.status.toString())
        }
        return wunderData
    }
}
