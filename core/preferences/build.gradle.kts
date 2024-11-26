plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            api(libs.multiplatformsettings.core)
            api(libs.multiplatformsettings.coroutines)
        }
    }
}