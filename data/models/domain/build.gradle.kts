plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.base)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
