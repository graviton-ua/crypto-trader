package ua.crypto.ui.home

import me.tatarka.inject.annotations.Component

@Component
interface HomeComponent {
    val homeViewModel: () -> HomeViewModel
}