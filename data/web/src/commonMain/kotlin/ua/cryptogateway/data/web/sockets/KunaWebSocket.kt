package ua.cryptogateway.data.web.sockets

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.web.BuildConfig
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class KunaWebSocket(
    dispatchers: AppCoroutineDispatchers,
    private val client: HttpClient,
) {
    private val dispatcher = dispatchers.io
    private val json = Json {
        encodeDefaults = true
        classDiscriminator = "event"
        explicitNulls = false
        ignoreUnknownKeys = true
    }
    private val eventRegexp = """\"event\":\"[^"]+@""".toRegex()

    private val eventChannel = MutableSharedFlow<KunaWebSocketEvent>(replay = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)


    fun flow(): Flow<Result<KunaWebSocketResponse>> = channelFlow {
        val session = client.webSocketSession(host = "ws-pro.kuna.io", /*port = 443,*/ path = "/socketcluster/")
        println("Connected to Kuna.SocketCluster server.")

        session.authenticate { session.close() }

        // Start
        launch(dispatcher) {
            println("Start listen for sending events")
            eventChannel.collect {
                session.sendEvent(it)
            }
        }

        launch(dispatcher) {
            println("Start listen for incoming frames")
            session.incoming.receiveAsFlow().collectLatest {
                when (it) {
                    is Frame.Text -> {
                        val text = it.readText()
                        println("Received: $text")
                        if (text.isEmpty()) {
                            session.send("")
                            return@collectLatest
                        }
                        if (text.contains("disconnect")) close()

                        val trimmed = text.replace(eventRegexp, "\"event\":\"")
                        val parsed = Result.runCatching { json.decodeFromString<KunaWebSocketResponse>(trimmed.also { println("Trimmed: $it") }) }
                        this@channelFlow.send(parsed)
                    }

                    else -> println("Unknown frame: $it")
                }
            }
        }

        awaitClose {
            /* do on cancellation */
            session.cancel()
        }
    }

    private suspend fun WebSocketSession.sendEvent(event: KunaWebSocketEvent): Unit = send(json.encodeToString(event).also { println("Sending event: $it") })
    private fun sendEvent(event: KunaWebSocketEvent) = eventChannel.tryEmit(event)

    fun subscribe(vararg channels: String) {
        channels.forEachIndexed { i, channel ->
            //TODO: cid should be taken NOT FROM INDEX
            sendEvent(KunaWebSocketEvent.Subscribe(KunaWebSocketEvent.Subscribe.Data(channel), cid = i + 3))
        }
    }


    private suspend fun DefaultClientWebSocketSession.authenticate(closeSocket: suspend () -> Unit) {
        sendEvent(KunaWebSocketEvent.Handshake(cid = 1))

        //TODO: Refactor it to have proper response handling
        // Step 2: Wait for Handshake Response
        var handshakeSuccessful = false
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val response = frame.readText()
                println("Received: $response")

                if (response.contains("\"rid\":1")) {
                    println("Handshake successful.")
                    handshakeSuccessful = true
                    break
                }
            }
        }

        // Step 3: Send Authentication Event
        if (handshakeSuccessful) {
            sendEvent(KunaWebSocketEvent.Login(apiKey = BuildConfig.KUNA_API_KEY, cid = 2))
        } else {
            println("Handshake failed. Cannot authenticate.")
            return
        }

        // Step 4: Wait for Handshake Response
        var authenticated = false
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val response = frame.readText()
                println("Received: $response")

                if (response.contains("\"rid\":2")) {
                    println("Authenticated successful.")
                    authenticated = true
                    break
                }
            }
        }

        if (!authenticated) closeSocket()
    }
}