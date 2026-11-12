import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("beatkit.project")
}

kotlin {
    // The iOS app links this module as `MeasurementKit.framework` (see ios-app, build phase "Compile Kotlin").
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "MeasurementKit"
            isStatic = true
            export(project(":beatkit-foundation"))
            export(project(":beatkit-module"))
            export(project(":measurementkit-model"))
            export(project(":measurementkit-module"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":beatkit-foundation"))
            api(project(":beatkit-module"))
            api(project(":measurementkit-model"))
            api(project(":measurementkit-module"))
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}
