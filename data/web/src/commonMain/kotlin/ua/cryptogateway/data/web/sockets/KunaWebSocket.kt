package ua.cryptogateway.data.web.sockets

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class KunaWebSocket(
    dispatchers: AppCoroutineDispatchers,
    private val client: HttpClient,
) {
    private val dispatcher = dispatchers.io

    private val eventChannel = MutableSharedFlow<KunaWebSocketEvent>(replay = 0, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)
    private val receiveChannel = MutableSharedFlow<String>()

    fun flow(): Flow<Unit> = channelFlow {
        val session = client.webSocketSession(host = "ws-pro.kuna.io", /*port = 443,*/ path = "/socketcluster/")
        val converter = session.converter ?: throw WebsocketConverterNotFoundException("No converter was found for websocket")

        launch(dispatcher) {
            eventChannel.collect {
                println("Send event: $it")
                session.send(Json.encodeToString(it).also { println("Send text: $it") })
            }
        }

        launch(dispatcher) {
            println("Start listen for incoming frames")
            session.incoming.receiveAsFlow()
                .onEach { println("Receive frame: $it") }
                .collect {
                    when (it) {
                        is Frame.Text -> {
                            val text = it.readText()
                            println("Receive text: $text")
                            if (text.isEmpty())
                                session.send("")
                            else receiveChannel.emit(text)

                            if (text.contains("disconnect")) {
                                close()
                            }
                        }

                        else -> println("Unknown frame: $it")
                    }
                }
        }

        authenticate { session.close() }

        awaitClose {
            /* do on cancellation */
            session.cancel()
        }
    }

    private fun sendEvent(event: KunaWebSocketEvent) = eventChannel.tryEmit(event)


    private fun CoroutineScope.authenticate(closeSocket: suspend () -> Unit) = launch(dispatcher) {
        println("Send Handshake event")
        sendEvent(KunaWebSocketEvent.Handshake(1))
        receiveChannel.firstOrNull()?.let {
            if (!it.contains("\"rid\":1")) {
                closeSocket()
                return@launch
            }
        } ?: return@launch

        println("Send Login event")
        sendEvent(KunaWebSocketEvent.Login(API_KEY, 2))
        receiveChannel.firstOrNull()?.let {
            if (!it.contains("\"rid\":2")) {
                closeSocket()
                return@launch
            }
        } ?: return@launch
    }


    companion object {
        //ApiKey: biRb/dUmozuosPTuQHecC6fTaQ0C+n0Iz1Dcms5KSE4= MY
        //ApiKey: 77HmE/lav4JZbiMy5e4IvNsOWINLH8dU4HJ0SDsNugk= FATHER
        private const val API_KEY = "77HmE/lav4JZbiMy5e4IvNsOWINLH8dU4HJ0SDsNugk="
    }
}