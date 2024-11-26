plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    packageName("ua.cryptogateway.data.web")

    buildConfigField(
        type = String::class.java,
        name = "TRAKT_DEBUG_CLIENT_SECRET",
        value = provider { properties["TIVI_DEBUG_TRAKT_CLIENT_SECRET"]?.toString() ?: "" },
    )
    buildConfigField(
        type = String::class.java,
        name = "TRAKT_DEBUG_CLIENT_ID",
        value = provider { properties["TIVI_DEBUG_TRAKT_CLIENT_ID"]?.toString() ?: "" },
    )
    buildConfigField(
        type = String::class.java,
        name = "TRAKT_CLIENT_SECRET",
        value = provider { properties["TIVI_TRAKT_CLIENT_SECRET"]?.toString() ?: "" },
    )
    buildConfigField(
        type = String::class.java,
        name = "TRAKT_CLIENT_ID",
        value = provider { properties["TIVI_TRAKT_CLIENT_ID"]?.toString() ?: "" },
    )
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                //api(libs.trakt.api)
                //api(projects.data.traktauth)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)

                api(libs.kotlinx.coroutines.core)

                api(libs.kotlininject.runtime)
            }
        }

        jvmMain {
            dependencies {
                api(libs.okhttp.okhttp)
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}
