package ua.crypto.core.logs

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import ua.crypto.core.appinitializers.AppInitializer
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers

@Inject
class LogbackInitializer(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val prefs: TraderPreferences,
) : AppInitializer {
    private val dispatcher = dispatchers.io

    override fun initialize() {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

        scope.launch(dispatcher) {
            prefs.loglevel.flow
                .collectLatest { value ->
                    rootLogger.updateConsoleThreshold(value.toLogLevel().levelStr)
                }
        }
        scope.launch(dispatcher) {
            prefs.fileLoglevel.flow
                .collectLatest { value ->
                    rootLogger.updateFileThreshold(value.toLogLevel().levelStr)
                }
        }
    }
}

private fun Logger.updateConsoleThreshold(level: String) {
    // Find the ConsoleAppender
    val consoleAppender = this.getAppender("CONSOLE") as? ConsoleAppender<*>
        ?: throw IllegalStateException("Console appender not found")

    // Find or add a ThresholdFilter
    val filter = consoleAppender.copyOfAttachedFiltersList
        .filterIsInstance<ThresholdFilter>()
        .firstOrNull() ?: return//ThresholdFilter().also { consoleAppender.addFilter(it) }

    // Update the filter level
    filter.setLevel(level)
    filter.start() // Restart the filter

    println("Updated console logging threshold to: $level")
}

private fun Logger.updateFileThreshold(level: String) {
    // Find the ConsoleAppender
    val fileAppender = this.getAppender("ROLLING") as? FileAppender<*>
        ?: throw IllegalStateException("File appender not found")

    // Find or add a ThresholdFilter
    val filter = fileAppender.copyOfAttachedFiltersList
        .filterIsInstance<ThresholdFilter>()
        .firstOrNull() ?: return//ThresholdFilter().also { consoleAppender.addFilter(it) }

    // Update the filter level
    filter.setLevel(level)
    filter.start() // Restart the filter

    println("Updated file logging threshold to: $level")
}

private fun LogLevel.toLogLevel(): Level = when (this) {
    LogLevel.DEBUG -> Level.DEBUG
    LogLevel.INFO -> Level.INFO
    LogLevel.WARNING -> Level.WARN
    LogLevel.ERROR -> Level.ERROR
}