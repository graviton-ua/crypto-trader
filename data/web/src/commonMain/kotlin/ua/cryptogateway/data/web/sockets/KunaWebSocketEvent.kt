package ua.cryptogateway.data.web.sockets

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal sealed interface KunaWebSocketEvent {
    val event: String
    val cid: Int


    @Serializable
    data class Handshake(
        override val cid: Int,
    ) : KunaWebSocketEvent {
        override val event: String = "#handshake"
        val data: Data = Data

        @Serializable
        data object Data
    }

    @Serializable
    data class Login(
        @Transient val apiKey: String = "",
        override val cid: Int,
    ) : KunaWebSocketEvent {
        override val event: String = "login"
        val data: String = apiKey
    }

    @Serializable
    data class Subscribe(
        @Transient val channel: String = "",
        override val cid: Int,
    ) : KunaWebSocketEvent {
        override val event: String = "#subscribe"
        val data: Data = Data(channel)

        @Serializable
        data class Data(val channel: String)
    }
}