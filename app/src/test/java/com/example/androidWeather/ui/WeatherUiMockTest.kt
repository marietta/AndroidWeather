package com.example.androidWeather.ui

import com.example.androidWeather.dto.wunderground.*
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherUiMockTest {

    @Test
    fun mockWundergroundData_withIcon35() {
        // Create a mock ObservationsCurrent with iconCode 35
        val mockObservationsCurrent = ObservationsCurrent(
            cloudCoverPhrase = "Partly Cloudy",
            dayOfWeek = "Friday",
            dayOrNight = "D",
            expirationTimeUtc = 1234567890L,
            iconCode = 35, // This is the target icon code
            iconCodeExtend = 3500,
            precip1Hour = 0.0,
            precip6Hour = 0.0,
            precip24Hour = 0.0,
            pressureAltimeter = 1013.25,
            pressureChange = 0.0,
            pressureMeanSeaLevel = 1013.25,
            pressureTendencyCode = 1,
            pressureTendencyTrend = "Steady",
            relativeHumidity = 50,
            snow1Hour = 0.0,
            snow6Hour = 0.0,
            snow24Hour = 0.0,
            sunriseTimeLocal = "06:00",
            sunriseTimeUtc = 1234500000L,
            sunsetTimeLocal = "18:00",
            sunsetTimeUtc = 1234560000L,
            temperature = 25,
            temperatureDewPoint = 15,
            temperatureFeelsLike = 26,
            temperatureHeatIndex = 26,
            temperatureMax24Hour = 30,
            temperatureMaxSince7Am = 28,
            temperatureMin24Hour = 20,
            temperatureWetBulbGlobe = 22,
            temperatureWindChill = 25,
            uvDescription = "Moderate",
            uvIndex = 5,
            validTimeLocal = "12:00",
            validTimeUtc = 1234530000L,
            visibility = 10.0,
            windDirection = 180,
            windDirectionCardinal = "S",
            windSpeed = 10,
            wxPhraseLong = "Mixed clouds and sun",
            wxPhraseMedium = "P. Cloudy",
            wxPhraseShort = "Partly Cloudy"
        )

        val mockV3Wx = V3WxObservations(
            id = "test_id",
            observationsCurrent = mockObservationsCurrent
        )

        val mockWunderData = WundergroundData(
            observations = emptyList(),
            observationsCurrent = listOf(mockV3Wx)
        )

        // Verify the mock is set up correctly
        assertEquals(35, mockWunderData.firstV3Observation?.iconCode)
        assertEquals("Mixed clouds and sun", mockWunderData.firstV3Observation?.wxPhraseLong)
    }
}
