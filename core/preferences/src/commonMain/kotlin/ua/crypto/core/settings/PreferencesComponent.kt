package ua.crypto.core.settings

import me.tatarka.inject.annotations.Provides
import ua.crypto.core.inject.ApplicationScope

expect interface PreferencesPlatformComponent

interface PreferencesComponent : PreferencesPlatformComponent {
    val preferences: TraderPreferences

    @ApplicationScope
    @Provides
    fun providePreferences(bind: TraderPreferencesImpl): TraderPreferences = bind
}
