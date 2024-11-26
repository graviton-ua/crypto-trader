plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.core.base)
            api(projects.core.logging.api)
            api(projects.core.preferences)
            api(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)
            implementation(libs.kotlininject.runtime)
        }
    }
}