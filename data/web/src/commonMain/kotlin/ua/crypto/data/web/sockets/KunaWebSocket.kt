package ua.crypto.data.web.sockets

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
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.web.BuildConfig
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
        val session = client.webSocketSession("wss://ws-pro.kuna.io/socketcluster/")
        Log.debug { "Connected to Kuna.SocketCluster server." }

        session.authenticate {
            Log.debug { "Incoming flow finished, closing websocket..." }
            session.close()
            this@channelFlow.close()
        }

        // Start
        launch(dispatcher) {
            Log.debug { "Start listen for sending events" }
            eventChannel.collect {
                session.sendEvent(it)
            }
        }

        launch(dispatcher) {
            Log.debug { "Start listen for incoming frames" }
            session.incoming.receiveAsFlow()
                .onCompletion {
                    Log.debug { "Incoming flow finished, closing websocket..." }
                    session.close()
                    this@channelFlow.close()
                }
                .collectLatest {
                    when (it) {
                        is Frame.Text -> {
                            val text = it.readText()

                            when {
                                text.isEmpty() -> {
                                    Log.debug { "Received ping message, sending pong..." }
                                    session.send("")
                                    return@collectLatest
                                }

                                text.contains("rid") -> {
                                    /* We received confirmation of successful subscription etc, ignore it */
                                    Log.debug { "Received success message for: $text" }
                                    return@collectLatest
                                }
                            }

                            Log.debug { "Received: $text" }

                            val parsed = measureTimedValue {
                                val trimmed = text.replace(eventRegexp, "\"event\":\"")
                                Result.runCatching { json.decodeFromString<KunaWebSocketResponse>(trimmed) }
                            }
                            this@channelFlow.send(parsed.value)
                            //Log.debug { "Emit response[decoding: ${parsed.duration}]: ${parsed.value}" }
                        }

                        is Frame.Ping -> {
                            Log.debug { "Received Frame.Ping, sending pong..." }
                            session.send("")
                        }

                        is Frame.Binary -> {
                            Log.debug { "Received Frame.Binary, ignoring..." }
                        }

                        is Frame.Close -> {
                            Log.debug { "Received Frame.Close, closing websocket..." }
                            session.close()
                            this@channelFlow.close()
                        }

                        else -> println("Unknown frame: $it")
                    }
                }
        }

        awaitClose {
            /* do on cancellation */
            session.cancel()
            Log.info { "Disconnected from Kuna.SocketCluster server." }
        }
    }

    private suspend fun WebSocketSession.sendEvent(event: KunaWebSocketEvent) {
        val s = measureTimedValue { json.encodeToString(event) }
        send(s.value)
        Log.debug { "Sent event[encoding: ${s.duration}]: ${s.value}" }
    }

    private fun sendEvent(event: KunaWebSocketEvent) = eventChannel.tryEmit(event)

    fun subscribe(vararg channels: Channel) {
        channels.forEach {
            sendEvent(KunaWebSocketEvent.Subscribe(KunaWebSocketEvent.Subscribe.Data(channel = it.tag), cid = cid.getAndIncrement()))
        }
    }


    private suspend fun DefaultClientWebSocketSession.authenticate(closeSocket: suspend () -> Unit) {
        // Step 1: Send Handshake event
        val handshakeCid = cid.getAndIncrement()
        sendEvent(KunaWebSocketEvent.Handshake(cid = handshakeCid))

        // Step 2: Wait for Handshake response
        val handshakeResult = incoming.receiveAsFlow().filterIsInstance<Frame.Text>().firstOrNull { frame ->
            frame.readText().contains("\"rid\":$handshakeCid")
        }

        if (handshakeResult == null) {
            Log.warn { "Handshake failed. Cannot proceed." }
            closeSocket()
            return
        } else Log.debug { "Success handshake" }

        // Step 3: Send Authentication event
        val authCid = cid.getAndIncrement()
        sendEvent(KunaWebSocketEvent.Login(apiKey = BuildConfig.KUNA_API_KEY, cid = authCid))

        // Step 4: Wait for Authentication response
        val authResult = incoming.receiveAsFlow().filterIsInstance<Frame.Text>().firstOrNull { frame ->
            frame.readText().contains("\"rid\":$authCid")
        }

        if (authResult == null) {
            Log.warn { "Authentication failed. Cannot proceed." }
            closeSocket()
            return
        } else Log.debug { "Success authentication" }
    }


    sealed class Channel(val tag: String) {
        data object ArrTicker : Channel("arrTicker")
        data class Ticker(val pair: String) : Channel("${pair.lowercase()}@ticker")
        data object ArrMiniTicker : Channel("arrMiniTicker")
        data class MiniTicker(val pair: String) : Channel("${pair.lowercase()}@miniTicker")
        data class Ohlcv(val pair: String) : Channel("${pair.lowercase()}@ohlcv")
        data class Trade(val pair: String) : Channel("${pair.lowercase()}@trade")
        data class AggTrade(val pair: String) : Channel("${pair.lowercase()}@aggTrade")


        data object Accounts : Channel("accounts")
    }
}