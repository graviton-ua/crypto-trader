package ua.cryptogateway.logs

import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.slf4j.SLF4JLogger
import ua.cryptogateway.appinitializers.AppInitializer

@Inject
class Log4KInitializer : AppInitializer {
    override fun initialize() {
        Log.loggers.clear()
        Log.loggers += SLF4JLogger()
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info")
    }
}