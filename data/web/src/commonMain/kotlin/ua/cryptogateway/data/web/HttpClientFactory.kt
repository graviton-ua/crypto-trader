package ua.cryptogateway.data.web

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class HttpClientFactory(
    //private val engine: HttpClientEngineFactory<*>,
) {
    fun build(): HttpClient = HttpClient(engineFactory = OkHttp).config {
        install(DefaultRequest) {
            url(BuildConfig.KUNA_BASE_URL)
            header("accept", "application/json")
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }
}