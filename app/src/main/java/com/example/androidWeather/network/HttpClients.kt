package com.example.androidWeather.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object HttpClients {
    private val json = Json {
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true
    }

    val default: HttpClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}
