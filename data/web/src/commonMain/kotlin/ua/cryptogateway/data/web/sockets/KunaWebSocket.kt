package ua.cryptogateway.data.web.sockets

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.web.BuildConfig
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.measureTimedValue

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
    private val cid = atomic(0)
    private val eventChannel = MutableSharedFlow<KunaWebSocketEvent>(replay = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)


    fun flow(): Flow<Result<KunaWebSocketResponse>> = channelFlow {
        val session = client.webSocketSession(host = "ws-pro.kuna.io", /*port = 443,*/ path = "/socketcluster/")
        Log.info(tag = TAG) { "Connected to Kuna.SocketCluster server." }

        session.authenticate { session.close() }

        // Start
        launch(dispatcher) {
            Log.debug(tag = TAG) { "Start listen for sending events" }
            eventChannel.collect {
                session.sendEvent(it)
            }
        }

        launch(dispatcher) {
            Log.debug(tag = TAG) { "Start listen for incoming frames" }
            session.incoming.receiveAsFlow().collectLatest {
                when (it) {
                    is Frame.Text -> {
                        val text = it.readText()
                        Log.debug(tag = TAG) { "Received: $text" }
                        if (text.isEmpty()) {
                            session.send("")
                            return@collectLatest
                        }
                        if (text.contains("disconnect")) close()


                        val parsed = measureTimedValue {
                            val trimmed = text.replace(eventRegexp, "\"event\":\"")
                            Log.debug(tag = TAG) { "Trimmed: $trimmed" }
                            Result.runCatching { json.decodeFromString<KunaWebSocketResponse>(trimmed) }
                        }
                        this@channelFlow.send(parsed.value)
                        Log.debug(tag = TAG) { "Emit response[decoding: ${parsed.duration}]: ${parsed.value}" }
                    }

                    else -> println("Unknown frame: $it")
                }
            }
        }

        awaitClose {
            /* do on cancellation */
            session.cancel()
            Log.info(tag = TAG) { "Disconnected from Kuna.SocketCluster server." }
        }
    }

    private suspend fun WebSocketSession.sendEvent(event: KunaWebSocketEvent) {
        val s = measureTimedValue { json.encodeToString(event) }
        send(s.value)
        Log.debug(tag = TAG) { "Sent event[encoding: ${s.duration}]: ${s.value}" }
    }

    private fun sendEvent(event: KunaWebSocketEvent) = eventChannel.tryEmit(event)

    fun subscribe(vararg channels: String) {
        channels.forEach {
            sendEvent(KunaWebSocketEvent.Subscribe(KunaWebSocketEvent.Subscribe.Data(channel = it), cid = cid.getAndIncrement()))
        }
    }


    private suspend fun DefaultClientWebSocketSession.authenticate(closeSocket: suspend () -> Unit) {
        val handshakeCid = cid.getAndIncrement()
        sendEvent(KunaWebSocketEvent.Handshake(cid = handshakeCid))

        //TODO: Refactor it to have proper response handling
        // Step 2: Wait for Handshake Response
        var handshakeSuccessful = false
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val response = frame.readText()
                println("Received: $response")

                if (response.contains("\"rid\":$handshakeCid")) {
                    println("Handshake successful.")
                    handshakeSuccessful = true
                    break
                }
            }
        }

        // Step 3: Send Authentication Event
        val authCid = cid.getAndIncrement()
        if (handshakeSuccessful) {
            sendEvent(KunaWebSocketEvent.Login(apiKey = BuildConfig.KUNA_API_KEY, cid = authCid))
        } else {
            println("Handshake failed. Cannot authenticate.")
            closeSocket()
            return
        }

        // Step 4: Wait for Handshake Response
        var authenticated = false
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val response = frame.readText()
                println("Received: $response")

                if (response.contains("\"rid\":$authCid")) {
                    println("Authenticated successful.")
                    authenticated = true
                    break
                }
            }
        }

        if (!authenticated) closeSocket()
    }


    companion object {
        private const val TAG = "KunaWebSocket"
    }
}