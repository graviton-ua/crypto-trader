package ua.cryptogateway.util

import kotlinx.coroutines.flow.*

internal class RecordingLoggerImpl(
    private val bufferSize: Int = 100,
) : RecordingLogger {
    private val _buffer = MutableStateFlow(ArrayDeque<LogMessage>(bufferSize))

    override val buffer: Flow<List<LogMessage>> = _buffer.asStateFlow()

    override fun v(throwable: Throwable?, message: () -> String) {
        addLog(LogMessage(Severity.Verbose, message(), throwable))
    }

    override fun d(throwable: Throwable?, message: () -> String) {
        addLog(LogMessage(Severity.Debug, message(), throwable))
    }

    override fun i(throwable: Throwable?, message: () -> String) {
        addLog(LogMessage(Severity.Info, message(), throwable))
    }

    override fun e(throwable: Throwable?, message: () -> String) {
        addLog(LogMessage(Severity.Error, message(), throwable))
    }

    override fun w(throwable: Throwable?, message: () -> String) {
        addLog(LogMessage(Severity.Warn, message(), throwable))
    }

    private fun addLog(logMessage: LogMessage) {
        _buffer.update { logs ->
            while (logs.size > bufferSize - 1) {
                logs.removeFirst()
            }
            logs += logMessage
            logs
        }
    }
}

internal object NoopRecordingLogger : RecordingLogger {
    override val buffer: StateFlow<List<LogMessage>> = MutableStateFlow(emptyList())
}
