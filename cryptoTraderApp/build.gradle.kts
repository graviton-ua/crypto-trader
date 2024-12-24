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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.navigation.compose)

            implementation(projects.domain)
            implementation(projects.shared)
            implementation(projects.common.ui.compose)

            implementation(projects.ui.home)
            implementation(projects.ui.configs)
            implementation(projects.ui.settings)
        }
        desktopMain.dependencies {
            implementation(projects.shared)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "ua.crypto.trader.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ua.crypto.trader"
            packageVersion = "1.0.0"
        }
    }
}
