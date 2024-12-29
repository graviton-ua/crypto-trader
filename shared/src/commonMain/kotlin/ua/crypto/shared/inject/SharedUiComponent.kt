package ua.crypto.shared.inject

import ua.crypto.ui.configs.ConfigsComponent
import ua.crypto.ui.home.HomeComponent
import ua.crypto.ui.services.ServicesComponent
import ua.crypto.ui.settings.SettingsComponent

interface SharedUiComponent : HomeComponent,
    ConfigsComponent,
    SettingsComponent,
    ServicesComponent