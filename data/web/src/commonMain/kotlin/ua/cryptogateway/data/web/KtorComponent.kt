package ua.cryptogateway.data.web

import io.ktor.client.*
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope

interface KtorComponent {
    @ApplicationScope
    @Provides
    fun provideHttpClient(
        factory: HttpClientFactory,
    ): HttpClient = factory.build()
}