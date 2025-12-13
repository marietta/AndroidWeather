package com.example.androidWeather

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WeatherWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val API_TYPE_KEY = "api_type"
        const val WEATHERAPI_TYPE = "Weatherapi"
        const val WUNDERGROUND_TYPE = "Wunderground"
        const val OUTPUT_DATA_KEY = "output_data"
    }

    override suspend fun doWork(): Result {
        val apiType = inputData.getString(API_TYPE_KEY) ?: return Result.failure()

        return try {
            when (apiType) {
                WEATHERAPI_TYPE -> {
                    val weatherapi = Weatherapi()
                    val forecast = weatherapi.fetch()
                    // In a real app, you would save this data to a database or SharedPreferences
                    // and then the UI would observe those changes.
                    Log.d("WeatherWorker", "Fetched Weatherapi data: ${forecast?.current?.tempC}°C")
                    Result.success()
                }

                WUNDERGROUND_TYPE -> {
                    val wunderground = Wunderground()
                    val wunderData = wunderground.fetch()
                    // In a real app, you would save this data to a database or SharedPreferences
                    // and then the UI would observe those changes.
                    Log.d(
                        "WeatherWorker",
                        "Fetched Wunderground data: ${wunderData?.observations?.firstOrNull()?.metric?.temp}°C"
                    )
                    Result.success()
                }

                else -> {
                    Log.e("WeatherWorker", "Unknown API type: $apiType")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherWorker", "Error fetching data for $apiType", e)
            Result.retry()
        }
    }
}