plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    packageName("ua.crypto.data.web")

    buildConfigField(
        type = String::class.java,
        name = "KUNA_BASE_URL",
        value = provider { properties["KUNA_BASE_URL"]?.toString() ?: "https://api.kuna.io" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_API_KEY",
        value = provider { System.getenv("KUNA_API_KEY") ?: properties["KUNA_API_KEY"]?.toString() ?: "" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_PUBLIC_KEY",
        value = provider { System.getenv("KUNA_PUBLIC_KEY") ?: properties["KUNA_PUBLIC_KEY"]?.toString() ?: "" },
    )
    buildConfigField(
        type = String::class.java,
        name = "KUNA_PRIVATE_KEY",
        value = provider { System.getenv("KUNA_PRIVATE_KEY") ?: properties["KUNA_PRIVATE_KEY"]?.toString() ?: "" },
    )
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.preferences)
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

tasks.register("printAllEnvVars") {
    doLast {
        System.getenv().toSortedMap { key1,key2-> key1.compareTo(key2) }.forEach { (key, value) ->
            println("$key = $value")
        }
    }
}
