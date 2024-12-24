package ua.crypto.ui.settings

import me.tatarka.inject.annotations.Component

@Component
interface SettingsComponent {
    val settingsViewModel: () -> SettingsViewModel
}