package ua.cryptogateway.data.web

import io.ktor.client.*
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope

expect interface KtorPlatformComponent

interface KtorComponent : KtorPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideHttpClient(
        factory: HttpClientFactory,
    ): HttpClient = factory.build()
}