plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.logging)

            api(projects.data.models.db)

            api(libs.kotlinx.datetime)
            // Need to force upgrade these for recent Kotlin support
            //api(libs.kotlinx.atomicfu)
            api(libs.kotlinx.coroutines.core)

            implementation(libs.kotlininject.runtime)

            api(libs.exposed.core)
            api(libs.exposed.jdbc)
            implementation(libs.exposed.migration)
            implementation(libs.exposed.kotlin.datetime)
        }

        jvmMain {
            dependencies {
                api(libs.hikari)
                implementation(libs.mssql.jdbc)
            }
        }
    }
}