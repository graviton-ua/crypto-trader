package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val dao: BalanceDao,
) : ViewModel() {
    private val dispatcher = dispatchers.io


    fun onClick() {
        viewModelScope.testRun()
    }


    private fun CoroutineScope.testRun() = launch(dispatcher) {

        // Example to do API call
        api.getTickers()
            .onSuccess { value ->
                Log.info(tag = TAG) { "Value: $value" }
            }
            .onFailure { exception ->
                Log.error(tag = TAG, throwable = exception) { "Some error happen" }
            }


        // Example fetching data from database
        val list = dao.getAll()
        Log.info(tag = TAG) { "Balance entity list: $list" }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}