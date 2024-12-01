package ua.cryptogateway.inject

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlin.reflect.KClass

// A generic ViewModelFactory using kotlin-inject
class KotlinInjectViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val creator = creator()
        if (modelClass == creator::class) {
            @Suppress("UNCHECKED_CAST")
            return creator as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Extension function to make ViewModel injection seamless
inline fun <reified T : ViewModel> ViewModelStoreOwner.injectViewModel(
    extras: CreationExtras = CreationExtras.Empty,
    noinline creator: () -> T,
): T {
    val factory = KotlinInjectViewModelFactory(creator)
    return ViewModelProvider.create(this, factory, extras)[T::class]
}

@Composable
inline fun <reified VM : ViewModel> injectViewModel(
    extras: CreationExtras = CreationExtras.Empty,
    noinline creator: () -> VM,
): VM {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
        ?: throw IllegalStateException("ViewModelStoreOwner not found")

    return viewModelStoreOwner.injectViewModel(extras = extras, creator = creator)
}