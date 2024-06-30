package com.example.androidWeather.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class OpenMeteoForecast(
    @SerialName("latitude") var latitude: Double? = null,
    @SerialName("longitude") var longitude: Double? = null,
    @SerialName("generationtime_ms") var generationtimeMs: Double? = null,
    @SerialName("utc_offset_seconds") var utcOffsetSeconds: Int? = null,
    @SerialName("timezone") var timezone: String? = null,
    @SerialName("timezone_abbreviation") var timezoneAbbreviation: String? = null,
    @SerialName("elevation") var elevation: Double? = null,
    @SerialName("current_units") var currentUnits: CurrentUnits? = CurrentUnits(),
    @SerialName("current") var current: Current? = Current(),
    @SerialName("minutely_15_units") var minutely15Units: Minutely15Units? = Minutely15Units(),
    @SerialName("minutely_15") var minutely15: Minutely15? = Minutely15(),
    @SerialName("hourly_units") var hourlyUnits: HourlyUnits? = HourlyUnits(),
    @SerialName("hourly") var hourly: Hourly? = Hourly()

)

@Serializable
data class CurrentUnits(

    @SerialName("time") var time: String? = null,
    @SerialName("interval") var interval: String? = null,
    @SerialName("temperature_2m") var temperature2m: String? = null

)

@Serializable

data class Current(

    @SerialName("time") var time: String? = null,
    @SerialName("interval") var interval: Int? = null,
    @SerialName("temperature_2m") var temperature2m: Double? = null

)

@Serializable

data class Minutely15Units(

    @SerialName("time") var time: String? = null,
    @SerialName("temperature_2m") var temperature2m: String? = null

)

@Serializable

data class Minutely15(

    @SerialName("time") var time: ArrayList<String> = arrayListOf(),
    @SerialName("temperature_2m") var temperature2m: ArrayList<Double> = arrayListOf()

)

@Serializable

data class HourlyUnits(

    @SerialName("time") var time: String? = null,
    @SerialName("temperature_2m") var temperature2m: String? = null,
    @SerialName("uv_index") var uvIndex: String? = null

)

@Serializable

data class Hourly(

    @SerialName("time") var time: ArrayList<String> = arrayListOf(),
    @SerialName("temperature_2m") var temperature2m: ArrayList<Double> = arrayListOf(),
    @SerialName("uv_index") var uvIndex: ArrayList<Double> = arrayListOf()

)





