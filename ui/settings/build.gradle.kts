plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  jvm()

  sourceSets {
    val jvmMain by getting

    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)

      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.lifecycle.viewmodel.compose)
      implementation(libs.androidx.navigation.compose)

      implementation(projects.domain)
      implementation(projects.shared)
    }
//    jvmMain.dependencies {
//      implementation(projects.shared)
//      implementation(compose.desktop.currentOs)
//      implementation(libs.kotlinx.coroutines.swing)
//    }
  }
}
