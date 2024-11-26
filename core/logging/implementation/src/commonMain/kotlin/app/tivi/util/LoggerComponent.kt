package app.tivi.util

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.app.ApplicationInfo
import ua.cryptogateway.app.Flavor
import ua.cryptogateway.appinitializers.AppInitializer
import ua.cryptogateway.inject.ApplicationScope

expect interface LoggerPlatformComponent

interface LoggerComponent : LoggerPlatformComponent {
    @ApplicationScope
    @Provides
    fun bindRecordingLogger(
        applicationInfo: ApplicationInfo,
    ): RecordingLogger = when {
        applicationInfo.debugBuild -> RecordingLoggerImpl()
        applicationInfo.flavor == Flavor.Qa -> RecordingLoggerImpl()
        else -> NoopRecordingLogger
    }

    @Provides
    @IntoSet
    fun provideCrashReportingInitializer(impl: CrashReportingInitializer): AppInitializer = impl
}
