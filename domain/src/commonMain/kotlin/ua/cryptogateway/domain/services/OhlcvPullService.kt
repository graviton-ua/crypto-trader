package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.logs.Log
import ua.cryptogateway.data.db.dao.OhlcvDao
import ua.cryptogateway.data.db.models.OhlcvEntity
import ua.cryptogateway.data.web.sockets.ChannelData
import ua.cryptogateway.data.web.sockets.KunaWebSocket
import ua.cryptogateway.data.web.sockets.KunaWebSocket.Channel
import ua.cryptogateway.data.web.sockets.KunaWebSocketResponse
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers

@ApplicationScope
@Inject
class OhlcvPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
    private val dao: OhlcvDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private var job: Job? = null
    private val data = MutableSharedFlow<Pair<String, ChannelData.Ohlcv.Data>?>(
        replay = 0, // No replay; emit only new values
        extraBufferCapacity = 100, // Buffer up to 100 messages
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drop the oldest message if buffer is full
    )


    init {
        scope.updateTable()
    }


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "Websocket job started" }

            webSocket.subscribe(Channel.Ohlcv("btc_usdt"), Channel.Ohlcv("doge_usdt"))  // Subscribe for doge_usdt@ohlcv

            webSocket.flow()
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
        }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "Websocket job completed (exception: ${it?.message})" }; job = null } }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateOhlcvTable() job started" }

        data.filterNotNull()
            .map { (pair, data) -> data.toEntity(pair) }
            .collect { entity ->
                dao.save(entity)
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateOhlcvTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "OhlcvPullService"
    }
}


private fun ChannelData.Ohlcv.Data.toEntity(pair: String): OhlcvEntity = OhlcvEntity(
    id = 0,
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