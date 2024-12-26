package ua.crypto.core.settings

interface TraderPreferences {

    val dbPort: Preference<String>
    val loglevel: Preference<LogLevel>

    val kunaApiKey: Preference<String>

    enum class LogLevel {
        DEBUG, INFO, WARNING, ERROR;

        companion object {
            internal fun toInt(level: LogLevel): Int = level.ordinal
            internal fun fromInt(ordinal: Int): LogLevel = LogLevel.entries[ordinal % LogLevel.entries.size]
        }
    }
}