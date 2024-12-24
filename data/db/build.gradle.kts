plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.preferences)
            implementation(projects.core.logging)

            implementation(projects.data.models)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            // Need to force upgrade these for recent Kotlin support
            //api(libs.kotlinx.atomicfu)
            api(libs.kotlinx.coroutines.core)

            implementation(libs.kotlininject.runtime)

            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.primitive)
        }

        jvmMain {
            dependencies {
                api(libs.hikari)
                api(libs.sqldelight.driver)
                implementation(libs.postgreSQL.jdbc)
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName = "ua.crypto.data.sql"
            dialect("app.cash.sqldelight:postgresql-dialect:2.0.2")
        }
    }
}