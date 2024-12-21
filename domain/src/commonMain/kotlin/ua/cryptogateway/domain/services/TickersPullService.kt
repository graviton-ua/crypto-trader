package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.data.web.models.KunaTicker
import ua.cryptogateway.data.web.sockets.ChannelData
import ua.cryptogateway.data.web.sockets.KunaWebSocket
import ua.cryptogateway.data.web.sockets.KunaWebSocket.Channel
import ua.cryptogateway.data.web.sockets.KunaWebSocketResponse
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers

@ApplicationScope
@Inject
class TickersPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
    private val dao: TickersDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private var job: Job? = null
    private val data = MutableSharedFlow<List<ChannelData.Ticker.Data>?>(
        replay = 0, // No replay; emit only new values
        extraBufferCapacity = 100, // Buffer up to 100 messages
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drop the oldest message if buffer is full
    )


    init {
        scope.updateTickersTable()
    }


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "Websocket job started" }

            webSocket.subscribe(Channel.ArrTicker)  // Subscribe for all tickers

            webSocket.flow()
                .mapNotNull { result ->
                    result
                        .onFailure { Log.warn(tag = TAG, throwable = it) }
                        .getOrNull()
                }
                .filterIsInstance(KunaWebSocketResponse.PublishMessage::class)
                .map { it.data.data }
                .filterIsInstance(ChannelData.ArrTicker::class)
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.tryEmit(it.data)
                }
        }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "Websocket job completed (exception: ${it?.message})" }; job = null } }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateTickersTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateTickersTable() job started" }

        data.filterNotNull()
            .map { it.map(ChannelData.Ticker.Data::toEntity) }
            .collect { list ->
                dao.save(list)
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateTickersTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "TickersPullService"
    }
}

private fun KunaTicker.toEntity(): TickerEntity = TickerEntity(pairName, priceHigh, priceAsk, priceBid, priceLow, priceLast, change, timestamp)

private fun ChannelData.Ticker.Data.toEntity(): TickerEntity = TickerEntity(
    pairName = pair,
    priceHigh = highPrice,
    priceAsk = bestAskPrice,
    priceBid = bestBidPrice,
    priceLow = lowPrice,
    priceLast = lastPrice,
    change = priceChange,
    timestamp = Clock.System.now(),
)