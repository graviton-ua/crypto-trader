package ua.crypto.data.web.utils

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.crypto.data.web.BuildConfig
import kotlin.text.Charsets.UTF_8

/**
 * Converts the [HttpResponse] into a [Result] of the specified type [T].
 *
 * This method checks the status code of the HTTP response to determine if the request was successful.
 * If the status code indicates success (2xx), it attempts to deserialize the response body into the specified type [T].
 * If deserialization or any other error occurs, it catches the exception and wraps it in a [Result.failure].
 *
 * @return A [Result] containing the deserialized response body if successful, or an exception if an error occurred.
 */
internal suspend inline fun <reified T> HttpResponse.asResult(): Result<T> {
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
private fun HttpStatusCode.isSuccess() = value in 200..299


/**
 * Adds private headers required for authenticated requests to
 * the Kuna API to the HTTP message builder.
 *
 * @param T The type of the body data.
 * @param path The API endpoint path for which the headers are being added.
 * @param body The request body that will be included in the signature.
 */
internal inline fun <reified T> HttpMessageBuilder.privateHeaders(
    path: String,
    body: T,
    publicKey: String = BuildConfig.KUNA_PUBLIC_KEY,
    privateKey: String = BuildConfig.KUNA_PRIVATE_KEY,
) {
    header("public-key", publicKey)
    header("account", "pro")
    val nonce = Clock.System.now().toEpochMilliseconds()
    header("nonce", nonce)
    val jsonBody = Json.encodeToString(value = body)
    header("signature", createSignature(path = path, nonce = nonce, body = jsonBody, privateKey = privateKey))
}

/**
 * Creates a cryptographic signature using HMAC-SHA384.
 *
 * @param path The API endpoint path that will be included in the signature.
 * @param nonce A unique value to ensure the signature's uniqueness.
 * @param body Optional request body that will be included in the signature.
 * @param privateKey The secret key used for generating the HMAC, defaults to a predefined value.
 * @return The generated signature as a hexadecimal string.
 */
private fun createSignature(
    path: String,
    nonce: Long,
    body: String?,
    privateKey: String,
): String {
    // Serialize the body to JSON
    val message = "$path$nonce$body"

    // Assuming you're targeting JVM and using java.security for HMAC
    val hmac = javax.crypto.Mac.getInstance("HmacSHA384")
    val keySpec = javax.crypto.spec.SecretKeySpec(privateKey.toByteArray(UTF_8), "HmacSHA384")
    hmac.init(keySpec)

    val hash = hmac.doFinal(message.toByteArray(UTF_8))

    // Convert the hash bytes to a hexadecimal string
    return hash.joinToString("") { byte -> "%02x".format(byte /*and 0xFF.toByte()*/) }
}