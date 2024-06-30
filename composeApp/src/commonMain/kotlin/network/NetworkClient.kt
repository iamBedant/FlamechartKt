import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import network.IntervalResponse

expect fun createPlatformHttpClient(): HttpClient

internal fun createHttpClient(enableLogging: Boolean): HttpClient {
    return createPlatformHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}

class NetworkClient(
    private val httpClient: HttpClient = createHttpClient(true)
) {
    suspend fun getIntervals(): IntervalResponse {
        return handleErrors {
            httpClient.get("http://localhost:3000/data") {
                contentType(ContentType.Application.Json)
            }
        }
    }
}