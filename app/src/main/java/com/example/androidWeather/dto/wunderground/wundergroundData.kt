package com.example.androidWeather.dto.wunderground

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WundergroundData(
    val observations: List<Observation>,
    var observationsCurrent: List<V3WxObservations?> = emptyList(),
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

@Serializable
data class V3WxObservations(
    val id: String,
    @SerialName("v3-wx-observations-current") var observationsCurrent: ObservationsCurrent? = null,
)

@Serializable
data class ObservationsCurrent(
    val cloudCeiling: Int? = null,
    val cloudCoverPhrase: String,
    val dayOfWeek: String,
    val dayOrNight: String,
    val expirationTimeUtc: Long,
    val iconCode: Int,
    val iconCodeExtend: Int,
    val obsQualifierCode: String? = null,
    val obsQualifierSeverity: Int? = null,
    val precip1Hour: Double,
    val precip6Hour: Double,
    val precip24Hour: Double,
    val pressureAltimeter: Double,
    val pressureChange: Double,
    val pressureMeanSeaLevel: Double,
    val pressureTendencyCode: Int,
    val pressureTendencyTrend: String,
    val relativeHumidity: Int,
    val snow1Hour: Double,
    val snow6Hour: Double,
    val snow24Hour: Double,
    val sunriseTimeLocal: String,
    val sunriseTimeUtc: Long,
    val sunsetTimeLocal: String,
    val sunsetTimeUtc: Long,
    val temperature: Int,
    val temperatureChange24Hour: Int? = null,
    val temperatureDewPoint: Int,
    val temperatureFeelsLike: Int,
    val temperatureHeatIndex: Int,
    val temperatureMax24Hour: Int,
    val temperatureMaxSince7Am: Int,
    val temperatureMin24Hour: Int,
    val temperatureWindChill: Int,
    val uvDescription: String,
    val uvIndex: Int,
    val validTimeLocal: String,
    val validTimeUtc: Long,
    val visibility: Double,
    val windDirection: Int,
    val windDirectionCardinal: String,
    val windGust: Int? = null, // Assuming windGust can be nullable
    val windSpeed: Int,
    val wxPhraseLong: String,
    val wxPhraseMedium: String,
    val wxPhraseShort: String
)