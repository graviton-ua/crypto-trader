package ua.hospes.cryptogateway.ui.configs

import me.tatarka.inject.annotations.Component

@Component
interface ConfigsComponent {
    val configsViewModel: () -> ConfigsViewModel
}