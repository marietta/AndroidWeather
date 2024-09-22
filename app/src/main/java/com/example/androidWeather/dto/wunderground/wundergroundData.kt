package com.example.androidWeather.dto.wunderground

import kotlinx.serialization.Serializable

@Serializable
data class WundergroundData(
    val observations: List<Observation>
)

@Serializable
data class Observation(
    val stationID: String,
    val obsTimeUtc: String,
    val obsTimeLocal: String,
    val neighborhood: String,
    val softwareType: String? = null,
    val country: String,
    val solarRadiation: Double? = null,
    val lon: Double,
    val realtimeFrequency: String? = null,
    val epoch: Long,
    val lat: Double,
    var uv: Double? = null,
    val winddir: Int,
    val humidity: Double,
    val qcStatus: Int,
    val metric: Metric
)

@Serializable
data class Metric(
    val temp: Double,
    val heatIndex: Double,
    val dewpt: Double,
    val windChill: Double,
    val windSpeed: Double,
    val windGust: Double,
    val pressure: Double,
    val precipRate: Double,
    val precipTotal: Double,
    val elev: Double
)
