package ua.cryptogateway.logs

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import ua.cryptogateway.appinitializers.AppInitializer
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.settings.TiviPreferences.LogLevel
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class LogbackInitializer(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val prefs: TiviPreferences,
) : AppInitializer {
    private val dispatcher = dispatchers.io

    override fun initialize() {
        scope.launch(dispatcher) {
            prefs.loglevel.flow
                .collectLatest { value ->
                    updateConsoleThreshold(value.toLogLevel().levelStr)
                }
        }
    }
}

private fun updateConsoleThreshold(level: String) {
    val context = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    // Find the ConsoleAppender
    val consoleAppender = rootLogger.getAppender("CONSOLE") as? ConsoleAppender<*>
        ?: throw IllegalStateException("Console appender not found")

    // Find or add a ThresholdFilter
    val filter = consoleAppender.copyOfAttachedFiltersList
        .filterIsInstance<ThresholdFilter>()
        .firstOrNull() ?: return//ThresholdFilter().also { consoleAppender.addFilter(it) }

    // Update the filter level
    filter.setLevel(level)
    filter.start() // Restart the filter

    // Restart the appender
    consoleAppender.stop()
    consoleAppender.start()

    println("Updated console logging threshold to: $level")
}

private fun LogLevel.toLogLevel(): Level = when (this) {
    LogLevel.DEBUG -> Level.DEBUG
    LogLevel.INFO -> Level.INFO
    LogLevel.WARNING -> Level.WARN
    LogLevel.ERROR -> Level.ERROR
}