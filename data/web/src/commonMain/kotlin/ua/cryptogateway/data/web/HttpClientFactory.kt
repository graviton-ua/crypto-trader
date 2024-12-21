package ua.cryptogateway.data.web

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.logs.Log

@Inject
class HttpClientFactory {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun build(): HttpClient = HttpClient(engineFactory = CIO).config {
        install(DefaultRequest) {
            url(BuildConfig.KUNA_BASE_URL)
            header("accept", "application/json")
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) = Log.debug(tag = HttpClient::class.qualifiedName ?: "HttpClient") { message }
            }
        }
        install(WebSockets) {
            pingIntervalMillis = 10_000 // 10 seconds ping interval
            contentConverter = KotlinxWebsocketSerializationConverter(json)
        }
    }
}