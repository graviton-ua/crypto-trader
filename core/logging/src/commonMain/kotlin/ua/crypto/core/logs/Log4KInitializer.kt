package ua.crypto.core.logs

import me.tatarka.inject.annotations.Inject
import saschpe.log4k.slf4j.SLF4JLogger
import ua.crypto.core.appinitializers.AppInitializer

@Inject
class Log4KInitializer : AppInitializer {
    override fun initialize() {
        saschpe.log4k.Log.loggers.clear()
        saschpe.log4k.Log.loggers += SLF4JLogger()
    }
}