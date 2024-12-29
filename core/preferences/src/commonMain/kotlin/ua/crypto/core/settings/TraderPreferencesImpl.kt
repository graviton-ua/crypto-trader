package ua.crypto.core.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalSettingsApi::class)
@Inject
class TraderPreferencesImpl(
    settings: Lazy<ObservableSettings>,
    private val coroutineScope: ApplicationCoroutineScope,
    private val dispatchers: AppCoroutineDispatchers,
) : TraderPreferences {
    private val settings: ObservableSettings by settings
    private val flowSettings by lazy { settings.value.toFlowSettings(dispatchers.io) }

    override val dbPort: Preference<String> by lazy { StringPreference(KEY_DB_PORT, "5432") }
    override val loglevel: Preference<LogLevel> by lazy { MappingIntPreference(KEY_LOG_LEVEL, LogLevel.INFO, LogLevel::fromInt, LogLevel::toInt) }
    override val fileLoglevel: Preference<LogLevel> by lazy {
        MappingIntPreference(KEY_FILE_LOG_LEVEL, LogLevel.INFO, LogLevel::fromInt, LogLevel::toInt)
    }
    override val disabledServices: Preference<List<String>> by lazy {
        MappingPreference(
            key = KEY_DISABLED_SERVICES, defaultValue = emptyList(),
            toValue = { s -> s.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toList() },
            fromValue = { list -> list.joinToString(",") },
        )
    }


    private inner class StringPreference(
        private val key: String,
        override val defaultValue: String = "",
    ) : Preference<String> {
        override suspend fun set(value: String) = withContext(dispatchers.io) { settings[key] = value }
        override suspend fun get(): String = withContext(dispatchers.io) { settings.getString(key, defaultValue) }
        override suspend fun update(block: suspend (String) -> String) = withContext(dispatchers.io) { set(block(get())) }

        override fun getNotSuspended(): String = settings.getString(key, defaultValue)

        override val flow: StateFlow<String> by lazy {
            flowSettings
                .getStringFlow(key, defaultValue)
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIMEOUT),
                    initialValue = getNotSuspended(),
                )
        }
    }

    private inner class IntPreference(
        private val key: String,
        override val defaultValue: Int? = null,
    ) : Preference<Int?> {
        override suspend fun set(value: Int?) = withContext(dispatchers.io) { settings[key] = value }

        override suspend fun get(): Int? = withContext(dispatchers.io) {
            if (defaultValue != null) settings.getInt(key, defaultValue) else settings.getIntOrNull(key)
        }

        override suspend fun update(block: suspend (Int?) -> Int?) = withContext(dispatchers.io) { set(block(get())) }

        override fun getNotSuspended(): Int? {
            return if (defaultValue != null) settings.getInt(key, defaultValue) else settings.getIntOrNull(key)
        }

        override val flow: StateFlow<Int?> by lazy {
            flowSettings
                .let { if (defaultValue != null) it.getIntFlow(key, defaultValue) else it.getIntOrNullFlow(key) }
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIMEOUT),
                    initialValue = getNotSuspended(),
                )
        }
    }

    private inner class BooleanPreference(
        private val key: String,
        override val defaultValue: Boolean = false,
    ) : Preference<Boolean> {
        override suspend fun set(value: Boolean) = withContext(dispatchers.io) { settings[key] = value }
        override suspend fun get(): Boolean = withContext(dispatchers.io) { settings.getBoolean(key, defaultValue) }
        override suspend fun update(block: suspend (Boolean) -> Boolean) = withContext(dispatchers.io) { set(block(get())) }

        override fun getNotSuspended(): Boolean = settings.getBoolean(key, defaultValue)

        override val flow: StateFlow<Boolean> by lazy {
            flowSettings
                .getBooleanFlow(key, defaultValue)
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIMEOUT),
                    initialValue = getNotSuspended(),
                )
        }
    }

    private inner class MappingPreference<V>(
        private val key: String,
        override val defaultValue: V,
        private val toValue: (String) -> V,
        private val fromValue: (V) -> String,
    ) : Preference<V> {
        override suspend fun set(value: V) = withContext(dispatchers.io) { settings[key] = fromValue(value) }
        override suspend fun get(): V = withContext(dispatchers.io) { settings.getStringOrNull(key)?.let(toValue) ?: defaultValue }
        override suspend fun update(block: suspend (V) -> V) = withContext(dispatchers.io) { set(block(get())) }

        override fun getNotSuspended(): V = settings.getStringOrNull(key)?.let(toValue) ?: defaultValue

        override val flow: StateFlow<V> by lazy {
            flowSettings.getStringOrNullFlow(key)
                .map { it?.let(toValue) ?: defaultValue }
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIMEOUT),
                    initialValue = getNotSuspended(),
                )
        }
    }

    private inner class MappingIntPreference<V>(
        private val key: String,
        override val defaultValue: V,
        private val toValue: (Int) -> V,
        private val fromValue: (V) -> Int,
    ) : Preference<V> {
        override suspend fun set(value: V) = withContext(dispatchers.io) { settings[key] = fromValue(value) }
        override suspend fun get(): V = withContext(dispatchers.io) { settings.getIntOrNull(key)?.let(toValue) ?: defaultValue }
        override suspend fun update(block: suspend (V) -> V) = withContext(dispatchers.io) { set(block(get())) }

        override fun getNotSuspended(): V = settings.getIntOrNull(key)?.let(toValue) ?: defaultValue

        override val flow: StateFlow<V> by lazy {
            flowSettings.getIntOrNullFlow(key)
                .map { it?.let(toValue) ?: defaultValue }
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIMEOUT),
                    initialValue = getNotSuspended(),
                )
        }
    }

    private companion object {
        val SUBSCRIBED_TIMEOUT = 20.seconds
    }
}

internal const val KEY_DB_PORT = "pref_db_port"
internal const val KEY_LOG_LEVEL = "pref_log_level"
internal const val KEY_FILE_LOG_LEVEL = "pref_file_log_level"
internal const val KEY_DISABLED_SERVICES = "pref_disabled_services"

private fun ObservableSettings.toggleBoolean(key: String, defaultValue: Boolean = false) {
    putBoolean(key, !getBoolean(key, defaultValue))
}
