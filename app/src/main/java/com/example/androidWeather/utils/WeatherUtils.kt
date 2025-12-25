package com.example.androidWeather.utils

import android.util.Log
import com.example.androidWeather.R


fun getDrawableResourceId(iconCode: Int? = 30, dayOrNight: String? = "d"): Int {
    val resourceName = "im_${dayOrNight}_$iconCode"
    return try {
        // Construct the resource name based on the icon code
        // Get the resource ID dynamically
        R.drawable::class.java.getField(resourceName).getInt(null)
    } catch (_: Exception) {
        Log.d("getDrawableResourceId","Missing icon: $resourceName")
        R.drawable.im_d_28 // Fallback if not found
    }
}
