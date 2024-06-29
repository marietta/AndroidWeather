package com.example.myapplication
import com.example.myapplication.dto.WeatherapiCurrent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

suspend fun main() {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    val response: HttpResponse = client.get("https://api.weatherapi.com/v1/current.json?q=Budapest") {
        parameter("key","1c0a7e979c9c46da9dd112808242606")
           }

    val data : WeatherapiCurrent = response.body()
    println(data.current?.tempC)



    client.close()
}