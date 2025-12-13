package com.example.androidWeather.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClients {
    val default: HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    allowStructuredMapKeys = true
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest { }
    }
}
