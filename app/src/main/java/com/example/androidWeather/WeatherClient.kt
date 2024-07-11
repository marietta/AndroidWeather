package com.example.androidWeather

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.androidWeather.dto.accuweather.AccuweatherApiItem
import com.example.androidWeather.dto.openMeteo.OpenMeteoForecast
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.Date
import java.util.Locale

interface Api<T> {
    val url: String
    val intervalInMinutes: Int
        get() = 15
    val data: MutableState<T>
    suspend fun fetch()

    companion object {
        val ktorClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    allowStructuredMapKeys = true
                })
            }
            defaultRequest {
                headers {
                    contentType(ContentType.Application.Json)
                }
            }
        }
    }
}

class OpenMeteo : Api<OpenMeteoForecast?> {
    override val data = mutableStateOf<OpenMeteoForecast?>(null)
    override val url: String
        get() = "https://api.open-meteo.com/v1/forecast?latitude=47.395&longitude=19.123" +
                "&current=temperature_2m&minutely_15=temperature_2m" +
                "&hourly=temperature_2m,uv_index&timezone=Europe%2FBerlin&forecast_days=1" +
                "&forecast_hours=1&forecast_minutely_15=4"

    override suspend fun fetch() {
        val response = Api.ktorClient.get(url)
        Log.d("OpenMeteoForecast", response.status.toString())
        data.value = Api.ktorClient.get(url).body()
    }
}

class Weatherapi : Api<WeatherapiForecast?> {
    override val data = mutableStateOf<WeatherapiForecast?>(null)
    override val url: String
        get() = "https://api.weatherapi.com/v1/forecast.json?key=1c0a7e979c9c46da9dd112808242606" +
                "&q=47.395,19.123&days=1"

    override suspend fun fetch() {
        val response = Api.ktorClient.get(url)
        Log.d("Weatherapi", response.status.toString())
        data.value = Api.ktorClient.get(url).body()
    }
}

class Accuweather : Api<AccuweatherApiItem?> {
    override val data = mutableStateOf<AccuweatherApiItem?>(null)
    override val url: String
        get() = "https://dataservice.accuweather.com/currentconditions/v1/189894" +
                "?apikey=ECcOavNTZp9XNXgGFwIcU4nAxLnOM0mA&details=true"

    override val intervalInMinutes: Int
        get() = 12 * 60
    private var nextFetch: Date = Date()

    override suspend fun fetch() {
        if (Date().after(nextFetch)) {

            val response = Api.ktorClient.get(url)
            val expires = response.headers["Expires"]
            Log.d("Accuweather", expires.toString())

            val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val expiredDate = sdf.parse(expires)

            val value: List<AccuweatherApiItem> = response.body()
            data.value = value.firstOrNull()
            nextFetch = expiredDate
        } else Log.d("Accuweather", "using cached")
    }
}
