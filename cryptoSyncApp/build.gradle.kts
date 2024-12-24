import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(projects.domain)
            implementation(projects.shared)
        }
        desktopMain.dependencies {
            implementation(projects.shared)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "ua.crypto.sync.resources"
    generateResClass = auto
}

compose.desktop {
    application {
        mainClass = "ua.crypto.sync.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ua.crypto.sync"
            packageVersion = "1.0.0"

            includeAllModules = true

            // Enable console for the application
            windows {
                console = true // Ensures the console opens when the app launches
                perUserInstall = true
                upgradeUuid = "9c72f767-74e5-48e7-98fc-f2210e1de82d"
            }
        }
    }
}