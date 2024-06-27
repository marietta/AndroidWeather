package com.example.myapplication1


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication1.dto.WeatherapiCurrent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val currentOrientation = LocalConfiguration.current.orientation
                val data = remember { mutableStateOf<WeatherapiCurrent?>(null) }

                LaunchedEffect(Unit) {
                    fetchData(data)
                }

                when (currentOrientation) {
                    Configuration.ORIENTATION_PORTRAIT -> data.value?.let { PortraitLayout(it) }
                    Configuration.ORIENTATION_LANDSCAPE -> data.value?.let { LandscapeLayout(it) }
                    else -> data.value?.let { PortraitLayout(it) } // Default to portrait if orientation is unknown
                }
            }
        }
    }

}


suspend fun fetchData(data: MutableState<WeatherapiCurrent?>) {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            headers {
                contentType(ContentType.Application.Json)
            }
            // this: DefaultRequestBuilder
        }
    }

    val response: HttpResponse =
        client.get("https://api.weatherapi.com/v1/current.json?q=Budapest") {
            parameter("key", "1c0a7e979c9c46da9dd112808242606")
        }

    data.value = response.body()
    client.close()
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        content()
    }
}

@Composable
fun LandscapeLayout(data: WeatherapiCurrent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    )
    {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${data.current?.tempC} °C",
                fontSize = 64.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${data.current?.condition?.text}",
                fontSize = 32.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = "https:${data.current?.condition?.icon}",
                contentDescription = "Landscape Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

        }
    }

}


@Composable
fun PortraitLayout(data: WeatherapiCurrent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            "${data.current?.tempC} °C",
            fontSize = 64.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )

        Text(
            "${data.current?.condition?.text}",
            fontSize = 32.sp,
        )

        AsyncImage(
            model = "https:${data.current?.condition?.icon}",
            contentDescription = "Landscape Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

    }
}
