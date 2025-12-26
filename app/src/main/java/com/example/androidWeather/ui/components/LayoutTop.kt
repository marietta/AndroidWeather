package com.example.androidWeather.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.androidWeather.dto.wunderground.WundergroundData
import kotlin.math.roundToInt

import androidx.compose.ui.res.stringResource
import com.example.androidWeather.R

@Composable
fun LayoutTop(
    wunderData: WundergroundData?,
    isLoading: Boolean = false,
) {
    if (isLoading) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.loading),
                fontSize = 48.sp,
            )
        }
    } else if (wunderData != null) {
        val intPart = wunderData.firstObservation?.metric?.temp?.roundToInt()
        if (intPart != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$intPart ",
                    fontSize = 142.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = stringResource(R.string.celsius_symbol),
                    fontSize = 48.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                )
            }
            Text(
                text = wunderData.firstV3Observation?.wxPhraseLong ?: "",
                fontSize = 48.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}
