import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

suspend fun main() {
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

    val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/posts/1")

    val post: Post = response.body()
    println(post.title)

    client.close()
}