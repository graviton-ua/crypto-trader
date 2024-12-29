package ua.crypto.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.inject.ApplicationScope
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.TickersDao
import ua.crypto.data.db.models.TickerEntity
import ua.crypto.data.web.sockets.ChannelData
import ua.crypto.data.web.sockets.KunaWebSocket
import ua.crypto.data.web.sockets.KunaWebSocket.Channel
import ua.crypto.data.web.sockets.KunaWebSocketResponse
import ua.crypto.data.web.sockets.retryWhenNotCancelled
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class KunaTickersPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
    private val dao: TickersDao,
) : TraderServiceInitializer {
    private val dispatcher = dispatchers.io
    private val _running = MutableStateFlow(false)
    private var job: Job? = null
    private val data = MutableSharedFlow<List<ChannelData.Ticker.Data>?>(
        replay = 0, // No replay; emit only new values
        extraBufferCapacity = 100, // Buffer up to 100 messages
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drop the oldest message if buffer is full
    )

    init {
        scope.updateTickersTable()
    }


    override val isRunning: StateFlow<Boolean> = _running

    override fun start() {
        if (job != null || _running.value) return
        _running.value = true
        job = scope.launch(dispatcher) {
            Log.debug { "Websocket job started" }

            webSocket.subscribe(Channel.ArrTicker)  // Subscribe for all tickers

            webSocket.flow()
                .retryWhenNotCancelled(1.seconds)
                .mapNotNull { result ->
                    result
                        .onFailure { Log.warn(throwable = it) }
                        .getOrNull()
                }
                .filterIsInstance(KunaWebSocketResponse.PublishMessage::class)
                .map { it.data.data }
                .filterIsInstance(ChannelData.ArrTicker::class)
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.tryEmit(it.data)
                }
        }.also {
            it.invokeOnCompletion {
                _running.value = false
                job = null
                Log.debug { "Websocket job completed (exception: ${it?.message})" }
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateTickersTable() = launch(dispatcher) {
        Log.debug { "updateTickersTable() job started" }

        data.filterNotNull()
            .map { it.map(ChannelData.Ticker.Data::toEntity) }
            .collect { list ->
                dao.save(list)
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateTickersTable() job completed (exception: ${it?.message})" } } }
}

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