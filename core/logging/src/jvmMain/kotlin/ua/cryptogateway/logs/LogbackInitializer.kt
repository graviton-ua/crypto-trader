package ua.cryptogateway.logs

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import saschpe.log4k.Log
import ua.cryptogateway.appinitializers.AppInitializer

@Inject
class LogbackInitializer : AppInitializer {
    override fun initialize() {
        val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        rootLogger.level = Level.DEBUG
    }
}