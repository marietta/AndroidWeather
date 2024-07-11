package com.example.androidWeather.dto.accuweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AccuweatherApiItem(

    @SerialName("LocalObservationDateTime") var localObservationDateTime: String? = null,
    @SerialName("EpochTime") var epochTime: Int? = null,
    @SerialName("WeatherText") var weatherText: String? = null,
    @SerialName("WeatherIcon") var weatherIcon: Int? = null,
    @SerialName("HasPrecipitation") var hasPrecipitation: Boolean? = null,
    @SerialName("PrecipitationType") var precipitationType: String? = null,
    @SerialName("IsDayTime") var isDayTime: Boolean? = null,
    @SerialName("Temperature") var temperature: Temperature? = Temperature(),
    @SerialName("RealFeelTemperature") var realFeelTemperature: RealFeelTemperature? = RealFeelTemperature(),
    @SerialName("RealFeelTemperatureShade") var realFeelTemperatureShade: RealFeelTemperatureShade? = RealFeelTemperatureShade(),
    @SerialName("RelativeHumidity") var relativeHumidity: Int? = null,
    @SerialName("IndoorRelativeHumidity") var indoorRelativeHumidity: Int? = null,
    @SerialName("DewPoint") var DewPoint: DewPoint? = DewPoint(),
    @SerialName("Wind") var wind: Wind? = Wind(),
    @SerialName("WindGust") var WindGust: WindGust? = WindGust(),
    @SerialName("UVIndex") var UVIndex: Int? = null,
    @SerialName("UVIndexText") var UVIndexText: String? = null,
    @SerialName("Visibility") var Visibility: Visibility? = Visibility(),
    @SerialName("ObstructionsToVisibility") var ObstructionsToVisibility: String? = null,
    @SerialName("CloudCover") var CloudCover: Int? = null,
    @SerialName("Ceiling") var ceiling: Ceiling? = Ceiling(),
    @SerialName("Pressure") var pressure: Pressure? = Pressure(),
    @SerialName("PressureTendency") var pressureTendency: PressureTendency? = PressureTendency(),
    @SerialName("Past24HourTemperatureDeparture") var past24HourTemperatureDeparture: Past24HourTemperatureDeparture? = Past24HourTemperatureDeparture(),
    @SerialName("ApparentTemperature") var ApparentTemperature: ApparentTemperature? = ApparentTemperature(),
    @SerialName("WindChillTemperature") var WindChillTemperature: WindChillTemperature? = WindChillTemperature(),
    @SerialName("WetBulbTemperature") var WetBulbTemperature: WetBulbTemperature? = WetBulbTemperature(),
    @SerialName("WetBulbGlobeTemperature") var WetBulbGlobeTemperature: WetBulbGlobeTemperature? = WetBulbGlobeTemperature(),
    @SerialName("Precip1hr") var precip1hr: Precip1hr? = Precip1hr(),
    @SerialName("PrecipitationSummary") var precipitationSummary: PrecipitationSummary? = PrecipitationSummary(),
    @SerialName("TemperatureSummary") var temperatureSummary: TemperatureSummary? = TemperatureSummary(),
    @SerialName("MobileLink") var mobileLink: String? = null,
    @SerialName("Link") var link: String? = null
)

@Serializable
data class Metric(

    @SerialName("Value") var value: Double? = null,
    @SerialName("Unit") var uUnit: String? = null,
    @SerialName("UnitType") var unitType: Int? = null,
    @SerialName("Phrase") var phrase: String? = null


)

@Serializable
data class Imperial(

    @SerialName("Value") var value: Double? = null,
    @SerialName("Unit") var unit: String? = null,
    @SerialName("UnitType") var unitType: Int? = null,
    @SerialName("Phrase") var phrase: String? = null

)

@Serializable
data class Temperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class RealFeelTemperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class RealFeelTemperatureShade(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class DewPoint(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Direction(

    @SerialName("Degrees") var degrees: Double? = null,
    @SerialName("Localized") var localized: String? = null,
    @SerialName("English") var english: String? = null

)

@Serializable
data class Speed(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Wind(

    @SerialName("Direction") var direction: Direction? = Direction(),
    @SerialName("Speed") var speed: Speed? = Speed()

)

@Serializable
data class WindGust(

    @SerialName("Speed") var speed: Speed? = Speed()

)

@Serializable
data class Visibility(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Ceiling(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Pressure(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class PressureTendency(

    @SerialName("LocalizedText") var localizedText: String? = null,
    @SerialName("Code") var code: String? = null

)

@Serializable
data class Past24HourTemperatureDeparture(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class ApparentTemperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class WindChillTemperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class WetBulbTemperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class WetBulbGlobeTemperature(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Precip1hr(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Precipitation(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class PastHour(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past3Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past6Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past9Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past12Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past18Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past24Hours(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class PrecipitationSummary(

    @SerialName("Precipitation") var precipitation: Precipitation? = Precipitation(),
    @SerialName("PastHour") var pastHour: PastHour? = PastHour(),
    @SerialName("Past3Hours") var past3Hours: Past3Hours? = Past3Hours(),
    @SerialName("Past6Hours") var past6Hours: Past6Hours? = Past6Hours(),
    @SerialName("Past9Hours") var past9Hours: Past9Hours? = Past9Hours(),
    @SerialName("Past12Hours") var past12Hours: Past12Hours? = Past12Hours(),
    @SerialName("Past18Hours") var past18Hours: Past18Hours? = Past18Hours(),
    @SerialName("Past24Hours") var past24Hours: Past24Hours? = Past24Hours()

)

@Serializable
data class Minimum(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Maximum(

    @SerialName("Metric") var metric: Metric? = Metric(),
    @SerialName("Imperial") var imperial: Imperial? = Imperial()

)

@Serializable
data class Past6HourRange(

    @SerialName("Minimum") var minimum: Minimum? = Minimum(),
    @SerialName("Maximum") var maximum: Maximum? = Maximum()

)

@Serializable
data class Past12HourRange(

    @SerialName("Minimum") var minimum: Minimum? = Minimum(),
    @SerialName("Maximum") var maximum: Maximum? = Maximum()

)

@Serializable
data class Past24HourRange(

    @SerialName("Minimum") var minimum: Minimum? = Minimum(),
    @SerialName("Maximum") var maximum: Maximum? = Maximum()

)

@Serializable
data class TemperatureSummary(

    @SerialName("Past6HourRange") var past6HourRange: Past6HourRange? = Past6HourRange(),
    @SerialName("Past12HourRange") var past12HourRange: Past12HourRange? = Past12HourRange(),
    @SerialName("Past24HourRange") var past24HourRange: Past24HourRange? = Past24HourRange()

)




