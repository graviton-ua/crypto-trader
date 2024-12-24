package ua.crypto.core.settings

import me.tatarka.inject.annotations.Provides
import ua.crypto.core.inject.ApplicationScope

expect interface PreferencesPlatformComponent

interface PreferencesComponent : PreferencesPlatformComponent {
    val preferences: TiviPreferences

    @ApplicationScope
    @Provides
    fun providePreferences(bind: TiviPreferencesImpl): TiviPreferences = bind
}
