package ua.cryptogateway.logs

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.appinitializers.AppInitializer

actual interface LoggerPlatformComponent {
    @Provides
    @IntoSet
    fun provideLogbackInitializer(impl: LogbackInitializer): AppInitializer = impl
}