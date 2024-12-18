package ua.cryptogateway.domain.services

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.web.sockets.KunaWebSocket
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

            webSocket.subscribe("doge_usdt@ohlcv")

            webSocket.flow().collect {
                Log.info(tag = TAG) { "websocket response: $it" }
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