package app.tivi.util

import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope

actual interface LoggerPlatformComponent {
    @Provides
    @ApplicationScope
    fun provideLogger(
        kermitLogger: KermitLogger,
        recordingLogger: RecordingLogger,
    ): Logger = CompositeLogger(kermitLogger, recordingLogger)

    @Provides
    fun bindSetCrashReportingEnabledAction(): SetCrashReportingEnabledAction {
        return NoopSetCrashReportingEnabledAction
    }
}

private object NoopSetCrashReportingEnabledAction : SetCrashReportingEnabledAction {
    override fun invoke(enabled: Boolean) {}
}
