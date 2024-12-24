package ua.crypto.shared.appinitializers

import me.tatarka.inject.annotations.Inject
import ua.crypto.core.appinitializers.AppSuspendedInitializer

@Inject
class AppSuspendedInitializers(
    private val initializers: Lazy<Set<AppSuspendedInitializer>>,
) : AppSuspendedInitializer {
    override suspend fun initialize() {
        for (initializer in initializers.value) {
            initializer.initialize()
        }
    }
}
