package ua.crypto.data.web.sockets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class KunaWebSocketEvent {
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

@Serializable
data class KunaWebSocketEventConfirm(
    val rid: Int,
    val data: Data? = null,
    val error: Error? = null,
) {
    @Serializable
    data class Data(
        val id: String,
        val pingTimeout: Int,
        val isAuthenticated: Boolean,
    )

    @Serializable
    data class Error(
        val code: String,
        val message: String,
    )
}

// {"rid":0}
// {"rid":1,"data":{"id":"Nv1gt4uGHwYAY4vPavlv","pingTimeout":20000,"isAuthenticated":false}}
// {"rid":2,"error":{"message":"Wrong api key or user blocked","extensions":{"code":"REQUEST_INVALID_KEY"},"code":"REQUEST_INVALID_KEY"}}