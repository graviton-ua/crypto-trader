package ua.crypto.core.settings

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import me.tatarka.inject.annotations.Provides
import ua.crypto.core.inject.ApplicationScope
import java.util.prefs.Preferences

actual interface PreferencesPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideSettings(delegate: Preferences): ObservableSettings = PreferencesSettings(delegate)
}