package ua.hospes.cryptogateway.ui.configs

import me.tatarka.inject.annotations.Component
import ua.hospes.cryptogateway.ui.configs.edit.ConfigEditViewModel

@Component
interface ConfigsComponent {
    val configsViewModel: () -> ConfigsViewModel
    val configEditViewModel: () -> ConfigEditViewModel
}