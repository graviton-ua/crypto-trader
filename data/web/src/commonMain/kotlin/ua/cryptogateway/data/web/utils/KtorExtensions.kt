package ua.cryptogateway.data.web.utils

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend inline fun <reified T> HttpResponse.asResult(): Result<T> {
    return try {
        // Check for successful status codes
        if (status.isSuccess()) {
            val body: T = this.body()  // Deserialize response body to T
            Result.success(body)
        } else {
            Result.failure(Exception("HTTP error: $status"))
        }
    } catch (e: Exception) {
        // Handle any exceptions during the request or deserialization
        Result.failure(e)
    }
}

// Extension to check if HTTP status is successful
fun HttpStatusCode.isSuccess() = value in 200..299