plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging)
                implementation(projects.core.preferences)

                api(projects.data.models)
                implementation(projects.data.db)
                implementation(projects.data.web)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
