package ua.crypto.core.logs

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.crypto.core.appinitializers.AppInitializer

expect interface LoggerPlatformComponent

interface LoggerComponent : LoggerPlatformComponent {
    @Provides
    @IntoSet
    fun provideLog4KInitializer(impl: Log4KInitializer): AppInitializer = impl
}