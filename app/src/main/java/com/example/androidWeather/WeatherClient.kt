package com.example.androidWeather

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import com.example.androidWeather.dto.accuweather.AccuweatherApiItem
import com.example.androidWeather.dto.openMeteo.OpenMeteoForecast
import com.example.androidWeather.dto.weatherapi.WeatherapiForecast
import com.example.androidWeather.dto.wunderground.WundergroundData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.*

interface Api<T> {
    val lat: Double
        get() = 47.395
    val lon: Double
        get() = 19.123
    val url: String
    val intervalInMinutes: Int
        get() = 10

    // Removed: val data: MutableState<T>
    suspend fun fetch(): T? // Modified to return T?

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
    // Removed: override val data = mutableStateOf<OpenMeteoForecast?>(null)
    override val url: String
        get() = "https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}" +
                "&current=temperature_2m&minutely_15=temperature_2m" +
                "&hourly=temperature_2m,uv_index&timezone=Europe%2FBerlin&forecast_days=1" +
                "&forecast_hours=1&forecast_minutely_15=4"

    override suspend fun fetch(): OpenMeteoForecast? { // Modified return type
        val response = Api.ktorClient.get(url)
        return if (response.status.value == 200) response.body()
        else {
            Log.d("OpenMeteoForecast", response.status.toString())
            null
        }
    }
}

class OpenWeather : Api<OpenMeteoForecast?> {
    // Removed: override val data = mutableStateOf<OpenMeteoForecast?>(null)
    private val apiKey = BuildConfig.OPENWEATHER_KEY

    override val url: String
        get() = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&appid=${apiKey}"

    override suspend fun fetch(): OpenMeteoForecast? { // Modified return type
        val response = Api.ktorClient.get(url)
        return if (response.status.value == 200) response.body()
        else {
            Log.d("OpenWeather", response.status.toString())
            null
        }
    }
}

class Weatherapi : Api<WeatherapiForecast?> {
    // Removed: override val data = mutableStateOf<WeatherapiForecast?>(null)
    private val apiKey = BuildConfig.WEATHERAPI_KEY

    override val url: String
        get() = "https://api.weatherapi.com/v1/forecast.json?key=${apiKey}" +
                "&q=${lat},${lon}&days=1"

    override suspend fun fetch(): WeatherapiForecast? { // Modified return type
        return try {
            val response = Api.ktorClient.get(url)
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

class Accuweather : Api<AccuweatherApiItem?> {
    // Removed: override val data = mutableStateOf<AccuweatherApiItem?>(null)
    private val apiKey = BuildConfig.ACCUWEATHER_KEY
    override val url: String
        get() = "https://dataservice.accuweather.com/currentconditions/v1/189894?apikey=${apiKey}"

    override val intervalInMinutes: Int
        get() = 12 * 60
    private var nextFetch: Date = Date()

    override suspend fun fetch(): AccuweatherApiItem? { // Modified return type
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
                val result = value.firstOrNull()
                nextFetch = expiredDate
                return result
            }
        } else Log.d("Accuweather", "using cached. Next fetch $nextFetch")
        return null
    }
}

class Wunderground : Api<WundergroundData?> {
    // Removed: override val data = mutableStateOf<WundergroundData?>(null)

    override val url: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=e1f10a1e78da46f5b10a1e78da96f525&stationId=IBUDAP507&numericPrecision=decimal&format=json&units=m"
    val url2: String
        get() = "https://api.weather.com/v2/pws/observations/current?apiKey=e1f10a1e78da46f5b10a1e78da96f525&stationId=IBUDAP576&numericPrecision=decimal&format=json&units=m"

    val observations: String
        get() = "https://api.weather.com/v3/aggcommon/v3-wx-observations-current?apiKey=e1f10a1e78da46f5b10a1e78da96f525&geocodes=${lat},${lon}&language=en-US&units=m&format=json"

    override suspend fun fetch(): WundergroundData? { // Modified return type
        var wunderData: WundergroundData? = null
//        val response1 = Api.ktorClient.get(url)
//        if (response1.status.value == 200) data.value = response1.body()
//        else Log.d("Wunderground", "IBUDAP507 "+response1.status.toString())

        val response2 = Api.ktorClient.get(url2)
        if (response2.status.value == 200) {
//            if (response1.status.value == 204)
            wunderData = response2.body()
            val apiResponse = Json.decodeFromString<WundergroundData>(response2.body())
            val uvIndex = apiResponse.observations.firstOrNull()?.uv
            if (uvIndex != null) {
                wunderData?.observations?.firstOrNull()?.uv = uvIndex
            } else {
                Log.d("Wunderground", "IBUDAP603 " + response2.status.toString())
            }
        }

        val response3 = Api.ktorClient.get(observations)
        if (response3.status.value == 200) {
            wunderData?.observationsCurrent = response3.body()
        } else {
            Log.d("Wunderground wx", response2.status.toString())
        }
        return wunderData
    }
}
