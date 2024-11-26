package ua.cryptogateway.util

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface RecordingLogger : Logger {
    val buffer: Flow<List<LogMessage>>
}

enum class Severity {
    Verbose,
    Debug,
    Info,
    Warn,
    Error,
    Assert,
}

data class LogMessage(
    val severity: Severity,
    val message: String,
    val throwable: Throwable?,
    val timestamp: Instant = Clock.System.now(),
)
