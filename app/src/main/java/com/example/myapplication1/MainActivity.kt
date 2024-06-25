package com.example.myapplication1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.HttpClient as KtorHttpClient

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
    val responseCode = remember { mutableIntStateOf(0) }
    val responseJson = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        val client = KtorHttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("https://jsonplaceholder.typicode.com/posts/1")
        responseCode.intValue = response.status.value
        responseJson.value = response.body()


        client.close()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Response Code: ${responseCode.intValue}",
            fontSize = 36.sp
        )

        Text(
            "Response Json: ${responseJson.value}",
            fontSize = 24.sp
        )
        Image(
            painter = painterResource(id = R.drawable.jetcasterhero),
            contentDescription = "Your image description",
            modifier = Modifier
                .height(200.dp)
        )

    }
}