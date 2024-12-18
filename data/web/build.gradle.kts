plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    packageName("ua.cryptogateway.data.web")

    buildConfigField(
        type = String::class.java,
        name = "KUNA_BASE_URL",
        value = provider { properties["KUNA_BASE_URL"]?.toString() ?: "https://api.kuna.io" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_PUBLIC_KEY",
        value = provider { properties["KUNA_PUBLIC_KEY"]?.toString() ?: "xFk60WymLUE1d8TzosFw3NWaOsWJok7R8t8SxNx2" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_PRIVAT_KEY",
        value = provider { properties["KUNA_PRIVAT_KEY"]?.toString() ?: "MxibPfs11MQhCHfSOos7feUuFJ0gZNx5njxOVxDm" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_API_KEY",
        value = provider { properties["KUNA_API_KEY"]?.toString() ?: "77HmE/lav4JZbiMy5e4IvNsOWINLH8dU4HJ0SDsNugk=" },
    )
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging)

                implementation(projects.data.models)
                implementation(libs.kotlinx.datetime)

                api(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization.json)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.websockets)

                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization.json)

                api(libs.kotlininject.runtime)
            }
        }
    }
}
