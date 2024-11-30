package ua.cryptogateway.data.web.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.models.web.KunaFee
import ua.cryptogateway.data.models.web.KunaMe
import ua.cryptogateway.data.models.web.KunaTicker
import ua.cryptogateway.data.web.utils.asResult
import ua.cryptogateway.data.web.utils.privateHeaders
import ua.cryptogateway.util.AppCoroutineDispatchers

/**
 * Class `KunaApi` handles various interactions with the Kuna API. This includes
 * fetching timestamps, fees, trading pairs, currencies, order books, and user-specific data.
 *
 * @constructor Creates an instance of `KunaApi`.
 * @param dispatchers The coroutine dispatchers used for executing the network requests.
 * @param client The HTTP client instance used for making requests.
 */
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
    suspend fun getTickers(vararg pairs: String): Result<List<KunaTicker>> = withContext(dispatcher) {
        client.get("/v4/markets/public/tickers") {
            if (pairs.isNotEmpty()) parameter("pairs", pairs.joinToString(","))
        }.asResult<KunaResponse<List<KunaTicker>>>().map { it.data }
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

    /**
     * Retrieves the user's personal information from the Kuna server.
     *
     * This is a suspension function that performs an HTTP GET request to the
     * "/v4/private/me" endpoint of the Kuna API. The function uses a coroutine
     * dispatcher to handle the network request asynchronously.
     *
     * @return [Result] containing the user's personal information wrapped in a [KunaMe] object.
     */
    suspend fun getMe(): Result<KunaMe> = withContext(dispatcher) {
        client.get("/v4/private/me") {
            privateHeaders(path = "/v4/private/me", body = Unit)
        }.asResult<KunaResponse<KunaMe>>().map { it.data }
    }
}