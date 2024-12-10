package ua.cryptogateway.logs

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
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
        val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        scope.launch(dispatcher) {
            prefs.loglevel.flow
                .collectLatest { value -> rootLogger.level = value.toLogLevel() }
        }
    }
}

private fun LogLevel.toLogLevel(): Level = when (this) {
    LogLevel.DEBUG -> Level.DEBUG
    LogLevel.INFO -> Level.INFO
    LogLevel.WARNING -> Level.WARN
    LogLevel.ERROR -> Level.ERROR
}