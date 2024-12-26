package ua.crypto.core.settings

interface TraderPreferences {

    val dbPort: Preference<String>
    val loglevel: Preference<LogLevel>

    val theme: Preference<Theme>
    val useDynamicColors: Preference<Boolean>

    val useLessData: Preference<Boolean>

    val libraryFollowedActive: Preference<Boolean>

    val upNextFollowedOnly: Preference<Boolean>

    val ignoreSpecials: Preference<Boolean>
    val reportAppCrashes: Preference<Boolean>
    val reportAnalytics: Preference<Boolean>

    val developerHideArtwork: Preference<Boolean>

    val episodeAiringNotificationsEnabled: Preference<Boolean>

    enum class LogLevel {
        DEBUG, INFO, WARNING, ERROR;

        companion object {
            internal fun toInt(level: LogLevel): Int = level.ordinal
            internal fun fromInt(ordinal: Int): LogLevel = LogLevel.entries[ordinal % LogLevel.entries.size]
        }
    }

    enum class Theme { LIGHT, DARK, SYSTEM }
}
