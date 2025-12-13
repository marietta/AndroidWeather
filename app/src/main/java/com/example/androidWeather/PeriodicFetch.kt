package com.example.androidWeather

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit


fun enqueuePeriodicFetch(context: Context, apiType: String, intervalInMinutes: Long) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
        repeatInterval = intervalInMinutes,
        repeatIntervalTimeUnit = TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .setInputData(workDataOf(WeatherWorker.API_TYPE_KEY to apiType))
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "${apiType}_fetch_work",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )
}