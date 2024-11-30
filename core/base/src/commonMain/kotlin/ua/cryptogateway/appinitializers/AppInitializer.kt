package ua.cryptogateway.appinitializers

fun interface AppInitializer {
    fun initialize()
}

fun interface AppSuspendedInitializer {
    suspend fun initialize()
}
