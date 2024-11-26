package ua.cryptogateway.data.web

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope

actual interface KtorPlatformComponent {

    @Provides
    @ApplicationScope
    fun provideHttpEngineFactory(): HttpClientEngineFactory<*> = OkHttp
}