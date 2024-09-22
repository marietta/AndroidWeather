package com.example.androidWeather

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.androidWeather.dto.accuweather.AccuweatherApiItem
import com.example.androidWeather.dto.openMeteo.OpenMeteoForecast
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import java.lang.Math.round
import java.util.*

interface Api<T> {
    val lat: Double
        get() = 47.395
    val lon: Double
        get() = 19.123
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
                    ignoreUnknownKeys = true
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
        get() = "https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}" +
                "&current=temperature_2m&minutely_15=temperature_2m" +
                "&hourly=temperature_2m,uv_index&timezone=Europe%2FBerlin&forecast_days=1" +
                "&forecast_hours=1&forecast_minutely_15=4"

    override suspend fun fetch() {
        val response = Api.ktorClient.get(url)
        Log.d("OpenMeteoForecast", response.status.toString())
        data.value = response.body()
    }
}

class OpenWeather : Api<OpenMeteoForecast?> {
    override val data = mutableStateOf<OpenMeteoForecast?>(null)
    private val apiKey = BuildConfig.OPENWEATHER_KEY

    override val url: String
        get() = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&appid=${apiKey}"

    override suspend fun fetch() {
        val response = Api.ktorClient.get(url)
        Log.d("OpenWeather", response.status.toString())
        if (response.status.value == 200) data.value = response.body()
    }
}

class Weatherapi : Api<WeatherapiForecast?> {
    override val data = mutableStateOf<WeatherapiForecast?>(null)
    private val apiKey = BuildConfig.WEATHERAPI_KEY

    override val url: String
        get() = "https://api.weatherapi.com/v1/forecast.json?key=${apiKey}" +
                "&q=${lat},${lon}&days=1"

    override suspend fun fetch() {
        val response = Api.ktorClient.get(url)
        Log.d("Weatherapi", response.status.toString())
        if (response.status.value == 200) data.value = response.body()
    }
}

class Accuweather : Api<AccuweatherApiItem?> {
    override val data = mutableStateOf<AccuweatherApiItem?>(null)
    private val apiKey = BuildConfig.ACCUWEATHER_KEY
    override val url: String
        get() = "https://dataservice.accuweather.com/currentconditions/v1/189894?apikey=${apiKey}"

    override val intervalInMinutes: Int
        get() = 12 * 60
    private var nextFetch: Date = Date()

    override suspend fun fetch() {
        if (Date().after(nextFetch)) {

            val response = Api.ktorClient.get(url)
            Log.d("Accuweather", response.status.toString())
            if (response.status == HttpStatusCode.OK) {
                Log.d("Accuweather", response.headers.toString())
                val expires = response.headers["Expires"]

                val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val expiredDate = sdf.parse(expires)
                Log.d("Accuweather", expiredDate.toString())

                val value: List<AccuweatherApiItem> = response.body()
                data.value = value.firstOrNull()
                nextFetch = expiredDate
            }

        } else Log.d("Accuweather", "using cached. Next fetch $nextFetch")
    }
}

class Wunderground : Api<WundergroundData?> {
    override val data = mutableStateOf<WundergroundData?>(null)

    override val url: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=e1f10a1e78da46f5b10a1e78da96f525&stationId=IBUDAP507&numericPrecision=decimal&format=json&units=m"
    val url2: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=e1f10a1e78da46f5b10a1e78da96f525&stationId=IBUDAP576&numericPrecision=decimal&format=json&units=m"

    override suspend fun fetch() {
        var response1 = Api.ktorClient.get(url)
        Log.d("Wunderground", response1.status.toString())
        if (response1.status.value == 200) {
            data.value = response1.body()
        }

        var response2 = Api.ktorClient.get(url2)
        Log.d("Wunderground", response2.status.toString())
        if (response2.status.value == 200) {
            val apiResponse = Json.decodeFromString<WundergroundData>(response2.body())
            val uvIndex = apiResponse.observations.firstOrNull()?.uv
            if (uvIndex != null) {
                data.value?.observations?.firstOrNull()?.uv = uvIndex
            }
        }
    }
}
