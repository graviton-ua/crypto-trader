package ua.cryptogateway.domain.services

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.web.sockets.ChannelData
import ua.cryptogateway.data.web.sockets.KunaWebSocket
import ua.cryptogateway.data.web.sockets.KunaWebSocket.Channel
import ua.cryptogateway.data.web.sockets.KunaWebSocketResponse
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers

@ApplicationScope
@Inject
class KunaWebsocketService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val webSocket: KunaWebSocket,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private var job: Job? = null


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {

            webSocket.subscribe(/*"arrTicker",*/ Channel.Ohlcv("doge_usdt"))

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
                            is ChannelData.ArrTicker -> Unit
                            is ChannelData.Ticker -> Unit
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


    companion object {
        private const val TAG = "KunaWebsocketService"
    }
}