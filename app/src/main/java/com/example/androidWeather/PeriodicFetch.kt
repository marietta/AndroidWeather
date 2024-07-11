package com.example.androidWeather

import android.os.Handler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import kotlinx.coroutines.runBlocking


@Composable
inline fun <reified T> periodicFetch(handler: Handler, api: Api<T>): MutableState<T> {
    LaunchedEffect(Unit) {
        val runnable = object : Runnable {
            override fun run() {
                runBlocking { api.fetch() }
                handler.postDelayed(this, api.intervalInMinutes * 60 * 1000L)
            }
        }
        handler.post(runnable)
    }
    return remember { api.data }
}