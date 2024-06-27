package com.example.myapplication1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                MakeApiCall()
            }
        }
    }
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
fun MakeApiCall() {
    val responseIconCode = remember { mutableIntStateOf(0) }
    val data = remember { mutableStateOf<WeatherapiCurrent?>(null) }

    LaunchedEffect(key1 = true) {
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

        responseIconCode.intValue = response.status.value
        data.value = response.body()

        client.close()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "${data.value?.current?.tempC} °C",
            fontSize = 56.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )

        Text(
            "${data.value?.current?.condition?.text}",
            fontSize = 32.sp
        )
        Image(
            painter = painterResource(
                id = R.drawable.w116
            ),
            contentDescription = "weather image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

    }
}
