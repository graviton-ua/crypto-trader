package ua.cryptogateway.logs

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.appinitializers.AppInitializer

interface LoggerComponent {
    @Provides
    @IntoSet
    fun provideLog4KInitializer(impl: Log4KInitializer): AppInitializer = impl
}