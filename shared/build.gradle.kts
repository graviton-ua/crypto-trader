import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.base)
                //api(projects.core.analytics)
                api(projects.core.logging)
                //api(projects.core.notifications.core)
                //api(projects.core.performance)
                //api(projects.core.permissions)
                //api(projects.core.powercontroller)
                api(projects.core.preferences)
                api(projects.data.db)
                api(projects.data.web)
                //api(projects.data.licenses)
                //api(projects.api.trakt)
                //api(projects.api.tmdb)
                api(projects.domain)
                //api(projects.tasks)

                //api(projects.common.imageloading)
                //api(projects.common.ui.compose)
                
                api(projects.ui.home)
                api(projects.ui.settings)
            }
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)


fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                add(
                    "ksp${target.targetName.capitalized()}$configurationNameSuffix",
                    dependencyNotation,
                )
            }
    }
}