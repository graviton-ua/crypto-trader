package ua.crypto.shared.inject

import me.tatarka.inject.annotations.Provides
import ua.crypto.core.app.ApplicationInfo
import ua.crypto.core.app.Flavor
import ua.crypto.core.inject.ApplicationScope
import java.io.File
import java.util.prefs.Preferences

actual interface SharedPlatformApplicationComponent {
    @ApplicationScope
    @Provides
    fun provideApplicationId(
        flavor: Flavor,
    ): ApplicationInfo = ApplicationInfo(
        packageName = "ua.cybergateway",
        debugBuild = true,
        flavor = flavor,
        versionName = "1.0.0",
        versionCode = 1,
        cachePath = { getCacheDir().absolutePath },
    )

    @ApplicationScope
    @Provides
    fun providePreferences(): Preferences = Preferences.userRoot().node("ua.cybergateway")
}

private fun getCacheDir(): File = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "cybergateway/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/cybergateway")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/cybergateway")
    else -> throw IllegalStateException("Unsupported operating system")
}

internal enum class OperatingSystem {
    Windows,
    Linux,
    MacOS,
    Unknown,
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> OperatingSystem.Windows
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                OperatingSystem.Linux
            }

            os.contains("mac") -> OperatingSystem.MacOS
            else -> OperatingSystem.Unknown
        }
    }
