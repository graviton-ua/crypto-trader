package ua.cryptogateway.util

import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.appinitializers.AppInitializer
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.settings.TiviPreferences

@Inject
class CrashReportingInitializer(
    private val preferences: Lazy<TiviPreferences>,
    private val action: Lazy<SetCrashReportingEnabledAction>,
    private val scope: ApplicationCoroutineScope,
) : AppInitializer {
    override fun initialize() {
        scope.launch {
            preferences.value.reportAppCrashes.flow
                .collect { action.value(it) }
        }
    }
}
