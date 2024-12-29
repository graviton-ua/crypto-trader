package ua.crypto.ui.services

import me.tatarka.inject.annotations.Component

@Component
interface ServicesComponent {
    val servicesViewModel: () -> ServicesViewModel
}