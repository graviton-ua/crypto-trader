package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.services.TickersPullService

@Inject
class HomeViewModel(
    private val tickersPuller: TickersPullService,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }
}