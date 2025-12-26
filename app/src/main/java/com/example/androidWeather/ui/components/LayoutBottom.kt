package com.example.androidWeather.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.androidWeather.R
import com.example.androidWeather.dto.wunderground.WundergroundData

@Composable
fun LayoutBottom(
    wunderData: WundergroundData?,
    getDrawableResourceId: (Int?) -> Int
) {
    HumidityRow(wunderData)
    DewPointRow(wunderData)
    WindRow(wunderData)
    WeatherIcon(wunderData, getDrawableResourceId)
    UVDisplay(wunderData)
}

@Composable
private fun HumidityRow(wunderData: WundergroundData?) {
    val humidity = wunderData?.firstObservation?.humidity?.toInt()
    val humidText = when {
        humidity == null -> "-"
        humidity < 40 -> stringResource(R.string.humidity_dry)
        humidity < 60 -> stringResource(R.string.humidity_good)
        humidity < 80 -> stringResource(R.string.humidity_humid)
        else -> stringResource(R.string.humidity_wet)
    }
    WeatherRow(
        valueText = humidity?.let { stringResource(R.string.humidity_format, it) } ?: "-",
        iconVector = Icons.Outlined.WaterDrop,
        iconDescription = "Humid Icon",
        statusText = humidText
    )
}

@Composable
private fun DewPointRow(wunderData: WundergroundData?) {
    val dewpt = wunderData?.firstObservation?.metric?.dewpt?.toInt()
    WeatherRow(
        valueText = stringResource(R.string.dew_point),
        iconResId = R.drawable.dew_point_24dp,
        iconDescription = "Dew Point Icon",
        statusText = dewpt?.let { stringResource(R.string.celsius_format, it) } ?: "-"
    )
}

@Composable
private fun WindRow(wunderData: WundergroundData?) {
    val windSpeed = wunderData?.firstV3Observation?.windSpeed
    val windText = when {
        windSpeed == null -> "-"
        windSpeed == 0 -> stringResource(R.string.wind_still)
        windSpeed < 10 -> stringResource(R.string.wind_breeze)
        windSpeed < 20 -> stringResource(R.string.wind_windy)
        else -> stringResource(R.string.wind_strong)
    }
    WeatherRow(
        valueText = windSpeed?.let { stringResource(R.string.wind_speed_format, it) } ?: "-",
        iconVector = Icons.Outlined.Air,
        iconDescription = "Air Icon",
        statusText = windText,
        iconSize = 64.dp
    )
}

@Composable
private fun WeatherRow(
    valueText: String,
    iconVector: ImageVector? = null,
    iconResId: Int? = null,
    iconDescription: String,
    statusText: String,
    iconSize: androidx.compose.ui.unit.Dp = 56.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = valueText, fontSize = 18.sp)
        WeatherIconWrapper(
            imageVector = iconVector,
            resourceId = iconResId,
            contentDescription = iconDescription,
            size = iconSize
        )
        Text(text = statusText, fontSize = 18.sp)
    }
}

@Composable
private fun WeatherIcon(
    wunderData: WundergroundData?,
    getDrawableResourceId: (Int?) -> Int
) {
    val iconCode = wunderData?.firstV3Observation?.iconCode

    if (iconCode != null) {
        val context = LocalContext.current

        val resId = remember(iconCode) {
            getDrawableResourceId(iconCode)
        }

        val model = if (resId != 0) {
            resId
        } else {
            "https://www.wunderground.com/static/i/c/v4/${iconCode}.svg"
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(0.4f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(model)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun UVDisplay(wunderData: WundergroundData?) {
    val currentObsV3 = wunderData?.firstV3Observation
    if (currentObsV3?.dayOrNight == "D") {
        val uv = wunderData.firstObservation?.uv?.toInt()
        if (uv != null) {
            val (fontWeight, color, fontSize) = when {
                uv >= 8 -> Triple(FontWeight.Bold, Color(209, 57, 74), 54.sp)
                uv >= 6 -> Triple(FontWeight.Bold, Color(252, 174, 0), 54.sp)
                uv >= 3 -> Triple(FontWeight.Normal, Color(255, 200, 0), 54.sp)
                else -> Triple(FontWeight.Light, Color.LightGray, 36.sp)
            }
            Text(
                text = stringResource(R.string.uv_format, uv),
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color
            )
        }
    }
}

@Composable
private fun WeatherIconWrapper(
    imageVector: ImageVector? = null,
    resourceId: Int? = null,
    contentDescription: String,
    size: androidx.compose.ui.unit.Dp = 56.dp
) {
    val modifier = Modifier
        .size(size)
        .padding(6.dp)
    if (imageVector != null) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, modifier = modifier)
    } else if (resourceId != null) {
        Icon(painter = painterResource(id = resourceId), contentDescription = contentDescription, modifier = modifier)
    }
}
