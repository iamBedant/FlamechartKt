import io.ktor.client.*

actual fun createPlatformHttpClient(): HttpClient {
    return HttpClient()
}