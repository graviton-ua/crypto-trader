package ua.cryptogateway.data.web.sockets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class KunaWebSocketEvent {
    abstract val cid: Int?

    // {"event":"#handshake","data":{ },"cid":1}
    @Serializable
    @SerialName("#handshake")
    data class Handshake(
        val data: Data = Data,
        override val cid: Int,
    ) : KunaWebSocketEvent() {
        @Serializable
        data object Data
    }

    // {"event":"login","data":"${BuildConfig.KUNA_API_KEY}","cid":2}
    @Serializable
    @SerialName("login")
    data class Login(
        @SerialName("data") val apiKey: String,
        override val cid: Int,
    ) : KunaWebSocketEvent()

    // {"event":"#subscribe","data":{"channel":"doge_usdt@ohlcv"},"cid":3}
    @Serializable
    @SerialName("#subscribe")
    data class Subscribe(
        val data: Data,
        override val cid: Int? = null,
    ) : KunaWebSocketEvent() {

        @Serializable
        data class Data(val channel: String)
    }
}