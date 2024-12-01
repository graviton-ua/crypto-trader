package ua.hospes.cryptogateway.ui.settings

import androidx.lifecycle.ViewModel
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.services.TickersPullService

@Inject
class SettingsViewModel(
    private val tickersPuller: TickersPullService,
) : ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}