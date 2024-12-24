package ua.crypto.ui.configs

import me.tatarka.inject.annotations.Component
import ua.crypto.ui.configs.edit.ConfigEditViewModel

@Component
interface ConfigsComponent {
    val configsViewModel: () -> ConfigsViewModel
    val configEditViewModel: () -> ConfigEditViewModel
}