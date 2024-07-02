package com.example.androidWeather.dto.weatherapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherapiForecast(

    @SerialName("location") var location: Location? = Location(),
    @SerialName("current") var current: Current? = Current(),
    @SerialName("forecast") var forecast: Forecast? = Forecast()

)

@Serializable
data class Location(

    @SerialName("name") var name: String? = null,
    @SerialName("region") var region: String? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("lat") var lat: Double? = null,
    @SerialName("lon") var lon: Double? = null,
    @SerialName("tz_id") var tzId: String? = null,
    @SerialName("localtime_epoch") var localtimeEpoch: Int? = null,
    @SerialName("localtime") var localtime: String? = null

)

@Serializable
data class Current(
    @SerialName("last_updated_epoch") var lastUpdatedEpoch: Int? = null,
    @SerialName("last_updated") var lastUpdated: String? = null,
    @SerialName("temp_c") var tempC: Double? = null,
    @SerialName("temp_f") var tempF: Double? = null,
    @SerialName("is_day") var isDay: Int? = null,
    @SerialName("condition") var condition: Condition? = Condition(),
    @SerialName("wind_mph") var windMph: Double? = null,
    @SerialName("wind_kph") var windKph: Double? = null,
    @SerialName("wind_degree") var windDegree: Int? = null,
    @SerialName("wind_dir") var windDir: String? = null,
    @SerialName("pressure_mb") var pressureMb: Double? = null,
    @SerialName("pressure_in") var pressureIn: Double? = null,
    @SerialName("precip_mm") var precipMm: Double? = null,
    @SerialName("precip_in") var precipIn: Double? = null,
    @SerialName("humidity") var humidity: Int? = null,
    @SerialName("cloud") var cloud: Int? = null,
    @SerialName("feelslike_c") var feelslikeC: Double? = null,
    @SerialName("feelslike_f") var feelslikeF: Double? = null,
    @SerialName("windchill_c") var windchillC: Double? = null,
    @SerialName("windchill_f") var windchillF: Double? = null,
    @SerialName("heatindex_c") var heatindexC: Double? = null,
    @SerialName("heatindex_f") var heatindexF: Double? = null,
    @SerialName("dewpoint_c") var dewpointC: Double? = null,
    @SerialName("dewpoint_f") var dewpointF: Double? = null,
    @SerialName("vis_km") var visKm: Double? = null,
    @SerialName("vis_miles") var visMiles: Double? = null,
    @SerialName("uv") var uv: Double? = null,
    @SerialName("gust_mph") var gustMph: Double? = null,
    @SerialName("gust_kph") var gustKph: Double? = null

)

@Serializable
data class Forecast(

    @SerialName("forecastday") var forecastday: ArrayList<Forecastday> = arrayListOf()

)

@Serializable
data class Forecastday(
    @SerialName("date") var date: String? = null,
    @SerialName("date_epoch") var dateEpoch: Int? = null,
    @SerialName("day") var day: Day? = Day(),
    @SerialName("astro") var astro: Astro? = Astro(),
    @SerialName("hour") var hour: ArrayList<Hour> = arrayListOf()

)

@Serializable
data class Astro(
    @SerialName("sunrise") var sunrise: String? = null,
    @SerialName("sunset") var sunset: String? = null,
    @SerialName("moonrise") var moonrise: String? = null,
    @SerialName("moonset") var moonset: String? = null,
    @SerialName("moon_phase") var moonPhase: String? = null,
    @SerialName("moon_illumination") var moonIllumination: Int? = null,
    @SerialName("is_moon_up") var isMoonUp: Int? = null,
    @SerialName("is_sun_up") var isSunUp: Int? = null

)

@Serializable
data class Day(
    @SerialName("maxtemp_c") var maxtempC: Double? = null,
    @SerialName("maxtemp_f") var maxtempF: Double? = null,
    @SerialName("mintemp_c") var mintempC: Double? = null,
    @SerialName("mintemp_f") var mintempF: Double? = null,
    @SerialName("avgtemp_c") var avgtempC: Double? = null,
    @SerialName("avgtemp_f") var avgtempF: Double? = null,
    @SerialName("maxwind_mph") var maxwindMph: Double? = null,
    @SerialName("maxwind_kph") var maxwindKph: Double? = null,
    @SerialName("totalprecip_mm") var totalprecipMm: Double? = null,
    @SerialName("totalprecip_in") var totalprecipIn: Double? = null,
    @SerialName("totalsnow_cm") var totalsnowCm: Double? = null,
    @SerialName("avgvis_km") var avgvisKm: Double? = null,
    @SerialName("avgvis_miles") var avgvisMiles: Double? = null,
    @SerialName("avghumidity") var avghumidity: Int? = null,
    @SerialName("daily_will_it_rain") var dailyWillItRain: Int? = null,
    @SerialName("daily_chance_of_rain") var dailyChanceOfRain: Int? = null,
    @SerialName("daily_will_it_snow") var dailyWillItSnow: Int? = null,
    @SerialName("daily_chance_of_snow") var dailyChanceOfSnow: Int? = null,
    @SerialName("condition") var condition: Condition? = Condition(),
    @SerialName("uv") var uv: Double? = null

)

@Serializable
data class Hour(
    @SerialName("time_epoch") var timeEpoch: Int? = null,
    @SerialName("time") var time: String? = null,
    @SerialName("temp_c") var tempC: Double? = null,
    @SerialName("temp_f") var tempF: Double? = null,
    @SerialName("is_day") var isDay: Int? = null,
    @SerialName("condition") var condition: Condition? = Condition(),
    @SerialName("wind_mph") var windMph: Double? = null,
    @SerialName("wind_kph") var windKph: Double? = null,
    @SerialName("wind_degree") var windDegree: Int? = null,
    @SerialName("wind_dir") var windDir: String? = null,
    @SerialName("pressure_mb") var pressureMb: Double? = null,
    @SerialName("pressure_in") var pressureIn: Double? = null,
    @SerialName("precip_mm") var precipMm: Double? = null,
    @SerialName("precip_in") var precipIn: Double? = null,
    @SerialName("snow_cm") var snowCm: Double? = null,
    @SerialName("humidity") var humidity: Int? = null,
    @SerialName("cloud") var cloud: Int? = null,
    @SerialName("feelslike_c") var feelslikeC: Double? = null,
    @SerialName("feelslike_f") var feelslikeF: Double? = null,
    @SerialName("windchill_c") var windchillC: Double? = null,
    @SerialName("windchill_f") var windchillF: Double? = null,
    @SerialName("heatindex_c") var heatindexC: Double? = null,
    @SerialName("heatindex_f") var heatindexF: Double? = null,
    @SerialName("dewpoint_c") var dewpointC: Double? = null,
    @SerialName("dewpoint_f") var dewpointF: Double? = null,
    @SerialName("will_it_rain") var willItRain: Int? = null,
    @SerialName("chance_of_rain") var chanceOfRain: Int? = null,
    @SerialName("will_it_snow") var willItSnow: Int? = null,
    @SerialName("chance_of_snow") var chanceOfSnow: Int? = null,
    @SerialName("vis_km") var visKm: Double? = null,
    @SerialName("vis_miles") var visMiles: Double? = null,
    @SerialName("gust_mph") var gustMph: Double? = null,
    @SerialName("gust_kph") var gustKph: Double? = null,
    @SerialName("uv") var uv: Double? = null

)

@Serializable
data class Condition(
    @SerialName("text") var text: String? = null,
    @SerialName("icon") var icon: String? = null,
    @SerialName("code") var code: Int? = null

)