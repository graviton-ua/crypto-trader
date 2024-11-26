plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.core.base)
            api(libs.log4k)
            implementation(libs.log4k.slf4j)
            implementation(libs.kotlininject.runtime)
        }
        jvmMain.dependencies {
            implementation(libs.slf4j.simple)
        }
    }
}