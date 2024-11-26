package ua.cryptogateway.data.web.api

import ua.cryptogateway.data.models.web.KunaFee
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.models.web.KunaMe
import ua.cryptogateway.data.web.BuildConfig
import ua.cryptogateway.data.web.utils.asResult
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.experimental.and
import kotlin.text.Charsets.UTF_8

@Inject
class KunaApi(
    dispatchers: AppCoroutineDispatchers,
    private val client: HttpClient,
) {
    private val dispatcher = dispatchers.io

    /**
     * Retrieves the current timestamp from the Kuna server.
     *
     * This is a suspension function that performs an HTTP GET request to the
     * "/v4/public/timestamp" endpoint of the Kuna API. The function uses the IO
     * dispatcher to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the server's timestamp.
     */
    suspend fun getTimestamp(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/public/timestamp") {

        }
    }

    /**
     * Retrieves the current fees from the Kuna server.
     *
     * This suspension function performs an HTTP GET request to the
     * "/v4/public/fees" endpoint of the Kuna API. It uses the IO dispatcher
     * to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the list of current fees.
     */
    suspend fun getFees(): Result<List<KunaFee>> = withContext(dispatcher) {
        client.get("/v4/public/fees").asResult<KunaResponse<List<KunaFee>>>().map { it.data }
    }

    /**
     * Retrieves all trading pairs from the Kuna server.
     *
     * This suspension function performs an HTTP GET request to the
     * "/v4/markets/public/getAll" endpoint of the Kuna API. It uses the IO
     * dispatcher to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the list of all trading pairs.
     */
    suspend fun getAllTraidingPairs(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/markets/public/getAll") {

        }
    }

    /**
     * Retrieves information about specific trading pairs from the Kuna server.
     *
     * This is a suspension function that performs an HTTP GET request to the
     * "/v4/markets/public/tickers" endpoint of the Kuna API. The function uses the
     * IO dispatcher to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the data for specific trading pairs.
     */
    suspend fun getTrickers(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/markets/public/tickers") {

        }
    }

    /**
     * Retrieves information about all available currencies from the Kuna server.
     *
     * This is a suspension function that performs an HTTP GET request to the
     * "/v4/public/currencies" endpoint of the Kuna API. It utilizes the IO dispatcher
     * to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the list of available currencies.
     */
    suspend fun getCurrencies(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/public/currencies") {

        }
    }

    /**
     * Retrieves the public order book from the Kuna server.
     *
     * This function performs an asynchronous HTTP GET request to the
     * "/v4/order/public/book" endpoint of the Kuna API, using the IO dispatcher
     * to handle the network request.
     *
     * @return [HttpResponse] containing the public order book data.
     */
    suspend fun getOrderBook(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/order/public/book") {

        }
    }

    /**
     * Retrieves the public trades book from the Kuna server.
     *
     * This is a suspension function that performs an HTTP GET request to the
     * "/v4/trade/public/book" endpoint of the Kuna API. It uses the IO dispatcher
     * to handle the network request asynchronously.
     *
     * @return [HttpResponse] containing the public trades book data.
     */
    suspend fun getTradesBook(): HttpResponse = withContext(dispatcher) {
        client.get("/v4/trade/public/book") {

        }
    }


    // Private endpoints

    suspend fun getMe(): Result<KunaMe> = withContext(dispatcher) {
        client.get("/v4/private/me") {
            header("public-key", BuildConfig.KUNA_PUBLIC_KEY)
            header("account", "pro")
            val nonce = Clock.System.now().toEpochMilliseconds()
            header("nonce", nonce)
            header(
                "signature",
                createSignature(
                    path = "/v4/private/me",
                    nonce = nonce,
                    body = JsonObject(content = emptyMap()),
                )
            )
        }.asResult<KunaResponse<KunaMe>>().map { it.data }
    }


    private inline fun <reified T> createSignature(
        path: String,
        nonce: Long,
        body: T? = null,
        privateKey: String = BuildConfig.KUNA_PRIVAT_KEY,
    ): String {
        // Serialize the body to JSON
        val jsonBody = Json.encodeToString(value = body ?: JsonObject(content = emptyMap()))
        val message = "$path$nonce$jsonBody"

        // Assuming you're targeting JVM and using java.security for HMAC
        val hmac = javax.crypto.Mac.getInstance("HmacSHA384")
        val keySpec = javax.crypto.spec.SecretKeySpec(privateKey.toByteArray(UTF_8), "HmacSHA384")
        hmac.init(keySpec)

        val hash = hmac.doFinal(message.toByteArray(UTF_8))

        // Convert the hash bytes to a hexadecimal string
        return hash.joinToString("") { byte -> "%02x".format(byte /*and 0xFF.toByte()*/) }
    }
}