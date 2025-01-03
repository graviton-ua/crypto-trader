package ua.crypto.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.inject.ApplicationScope
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.CandlesDao
import ua.crypto.data.db.models.CandleEntity
import ua.crypto.data.models.CryptoPlatform
import ua.crypto.data.web.sockets.ChannelData
import ua.crypto.data.web.sockets.KunaWebSocket
import ua.crypto.data.web.sockets.KunaWebSocket.Channel
import ua.crypto.data.web.sockets.KunaWebSocketResponse
import ua.crypto.data.web.sockets.retryWhenNotCancelled
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class KunaCandlePullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
    private val dao: CandlesDao,
) : SyncServiceInitializer {
    private val dispatcher = dispatchers.io
    private val _running = MutableStateFlow(false)
    private var job: Job? = null
    private val data = MutableSharedFlow<Pair<String, ChannelData.Ohlcv.Data>?>(
        replay = 0, // No replay; emit only new values
        extraBufferCapacity = 100, // Buffer up to 100 messages
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drop the oldest message if buffer is full
    )

    init {
        scope.updateTable()
    }


    override val isRunning: StateFlow<Boolean> = _running

    override fun start() {
        if (job != null || _running.value) return
        _running.value = true
        job = scope.launch(dispatcher) {
            Log.debug { "Websocket job started" }

            webSocket.subscribe(Channel.Ohlcv("btc_usdt"), Channel.Ohlcv("doge_usdt"))  // Subscribe for doge_usdt@ohlcv

            webSocket.flow()
                .retryWhenNotCancelled(1.seconds)
                .mapNotNull { result ->
                    result
                        .onFailure { Log.warn(throwable = it) }
                        .getOrNull()
                }
                .filterIsInstance(KunaWebSocketResponse.PublishMessage::class)
                .mapNotNull {
                    val channel = it.data.channel
                    when (val data = it.data.data) {
                        is ChannelData.Ohlcv -> {
                            val pair = channel.split("@").firstOrNull() ?: channel
                            pair to data.data
                        }

                        else -> null
                    }
                }
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.tryEmit(it)
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


    private fun CoroutineScope.updateTable() = launch(dispatcher) {
        Log.debug { "updateCandlesTable() job started" }

        data.filterNotNull()
            .map { (pair, data) -> data.toEntity(pair) }
            .collect { entity ->
                dao.save(entity)
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateCandlesTable() job completed (exception: ${it?.message})" } } }
}

private fun ChannelData.Ohlcv.Data.toEntity(pair: String): CandleEntity = CandleEntity(
    platform = CryptoPlatform.KUNA,
    pair = pair.uppercase(),
    openTime = Instant.fromEpochMilliseconds(openTime),
    closeTime = Instant.fromEpochMilliseconds(closeTime),
    trades = trades,
    openPrice = openPrice,
    closePrice = closePrice,
    highPrice = highPrice,
    lowPrice = lowPrice,
    volume = volume
)