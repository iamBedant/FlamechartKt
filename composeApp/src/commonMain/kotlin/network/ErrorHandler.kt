
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T> handleErrors(
    crossinline response: suspend () -> HttpResponse
): T = withContext(Dispatchers.Unconfined) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw NetworkException(NetworkError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw NetworkException(NetworkError.ClientError)
        500 -> throw NetworkException(NetworkError.ServerError)
        else -> throw NetworkException(NetworkError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        println(e)
        throw NetworkException(NetworkError.ServerError)
    }

}