package com.example.androidWeather.ui.components

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.SvgDecoder
import coil.intercept.Interceptor
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.example.androidWeather.dto.wunderground.ObservationsCurrent
import com.example.androidWeather.dto.wunderground.V3WxObservations
import com.example.androidWeather.dto.wunderground.WundergroundData
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class WeatherIconTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoilApi::class)
    private lateinit var engine: FakeImageLoaderEngine

    @OptIn(ExperimentalCoilApi::class)
    @Before
    fun setup() {
        engine = FakeImageLoaderEngine.Builder()
            .intercept("https://www.wunderground.com/static/i/c/v4/35.svg", ColorDrawable(Color.RED))
            .build()
    }

    @Test
    fun weatherIcon_withCode35_showsIcon() {
        // Setup mock data with icon code 35
        val mockObservationsCurrent = ObservationsCurrent(
            cloudCoverPhrase = "Partly Cloudy",
            dayOfWeek = "Friday",
            dayOrNight = "D",
            expirationTimeUtc = 1234567890L,
            iconCode = 35,
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

        val mockWunderData = WundergroundData(
            observations = emptyList(),
            observationsCurrent = listOf(V3WxObservations(id = "test", observationsCurrent = mockObservationsCurrent))
        )

        // Verify that the Weather Icon is displayed and has the correct URL
        val expectedUrl = "https://www.wunderground.com/static/i/c/v4/35.svg"

        val requests = mutableListOf<ImageRequest>()
        val debugEngine = object : Interceptor {
            override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
                requests.add(chain.request)
                return SuccessResult(
                    drawable = ColorDrawable(Color.RED),
                    request = chain.request,
                    dataSource = coil.decode.DataSource.MEMORY
                )
            }
        }

        // Render the LayoutBottom (which contains WeatherIcon)
        lateinit var imageLoader: ImageLoader
        composeTestRule.setContent {
            val context = LocalContext.current
            imageLoader = remember {
                ImageLoader.Builder(context)
                    .components { add(debugEngine) }
                    .build()
            }
            LayoutBottom(
                wunderData = mockWunderData,
                getDrawableResourceId = { 0 }, // Simulate missing local resource
                imageLoader = imageLoader
            )
        }

        // Wait for Coil to make the request
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Weather Icon")
            .assertIsDisplayed()

        // Check if the intercepted requests contain the expected URL and decoder
        assertTrue(
            "Request for $expectedUrl was not intercepted",
            requests.any { it.data == expectedUrl })
        assertTrue(
            "SvgDecoder.Factory should be used",
            requests.any { it.decoderFactory is SvgDecoder.Factory })
    }
}
