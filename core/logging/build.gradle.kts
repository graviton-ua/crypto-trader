plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.preferences)
            implementation(libs.log4k)
            implementation(libs.log4k.slf4j)
            implementation(libs.kotlininject.runtime)
        }
        jvmMain.dependencies {
            implementation(libs.logback.classic)
        }
    }
}