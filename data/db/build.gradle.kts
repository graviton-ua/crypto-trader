plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.logging)
            //api(projects.data.db)

            api(libs.kotlinx.datetime)
            // Need to force upgrade these for recent Kotlin support
            //api(libs.kotlinx.atomicfu)
            api(libs.kotlinx.coroutines.core)

            implementation(libs.kotlininject.runtime)

            implementation(libs.exposed.core)
            //implementation(libs.sqldelight.primitive)
            //implementation(libs.paging.common)
        }

        jvmMain {
            dependencies {
                implementation(libs.hikari)
                implementation(libs.mssql.jdbc)
            }
        }
    }
}