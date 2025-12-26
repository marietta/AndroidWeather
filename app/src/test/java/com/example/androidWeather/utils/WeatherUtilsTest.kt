package com.example.androidWeather.utils

import org.junit.Test
import org.junit.Assert.assertEquals

class WeatherUtilsTest {

    @Test
    fun getDrawableResourceId_withIcon35_returnsExpectedValue() {
        // The getDrawableResourceId function uses reflection to find R.drawable.im_35.
        // In a pure unit test, R.drawable.im_35 may not be available or may have a different ID.
        // However, we can verify that calling it with 35 doesn't crash and returns 0 if missing.
        
        val iconCode = 35
        val resId = getDrawableResourceId(iconCode)
        
        // Since im_35 is likely missing in the current project (based on project structure),
        // it should return 0.
        assertEquals(0, resId)
    }
}
