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
)
include(
    ":data:db",
    ":data:web",
    ":data:models:web",
    ":data:models:db",
    ":data:models:domain",
)
include(":domain")
include(":shared")
include(":composeApp")