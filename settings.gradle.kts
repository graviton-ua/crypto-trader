rootProject.name = "CryptoGateway"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(
    ":core:base",
    ":core:logging",
    ":core:preferences",
    ":core:compose-inject",
)
include(
    ":data:db",
    ":data:web",
    ":data:models",
)
include(":domain")
include(":shared")
include(":common:ui:compose")
include(
    ":ui:home",
    ":ui:configs",
    ":ui:settings",
)
include(":composeApp")