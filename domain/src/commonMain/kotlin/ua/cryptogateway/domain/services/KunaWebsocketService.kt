package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.data.web.sockets.ChannelData
import ua.cryptogateway.data.web.sockets.KunaWebSocket
import ua.cryptogateway.data.web.sockets.KunaWebSocketResponse
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.measureTimedValue

@ApplicationScope
@Inject
class KunaWebsocketService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
    private val botConfigsDao: BotConfigsDao,
    private val tickersDao: TickersDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private var job: Job? = null


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {

            webSocket.subscribe(/*"arrTicker",*/ "doge_usdt@ohlcv")

            webSocket.flow()
                .mapNotNull { result ->
                    result
                        .onSuccess { Log.info(tag = TAG) { "Websocket response: $it" } }
                        .onFailure { Log.warn(tag = TAG, throwable = it) }
                        .getOrNull()
                }
                .filterIsInstance(KunaWebSocketResponse.PublishMessage::class)
                .collect { message ->
                    supervisorScope {
                        val channel = message.data.channel
                        val data = message.data.data
                        when (data) {
                            is ChannelData.ArrTicker -> saveTickers(data.data)
                            is ChannelData.Ticker -> saveTickers(listOf(data.data))
                            is ChannelData.ArrMiniTicker -> Unit
                            is ChannelData.MiniTicker -> Unit
                            is ChannelData.AggTrade -> Unit
                            is ChannelData.Ohlcv -> Unit
                            is ChannelData.Trade -> Unit
                        }
                    }
                }
        }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "websocket job completed (exception: ${it?.message})" }; job = null } }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.saveTickers(data: List<ChannelData.Ticker.Data>) = launch {
        val measured = measureTimedValue {
            tickersDao.save(data.map(ChannelData.Ticker.Data::toEntity))
        }
        measured.value
            .onSuccess { Log.debug(tag = TAG) { "Tickers updated [${measured.duration}]" } }
            .onFailure { Log.error(tag = TAG, throwable = it) }
    }


    companion object {
        private const val TAG = "KunaWebsocketService"
    }
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