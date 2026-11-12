import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Convention plugin shared by every Kotlin Multiplatform module of the client, the counterpart of the
 * `org.beatkit.gradle.project` plugin in the production workspace.
 *
 * Targets: Android (library), iOS (device + Apple Silicon simulator) and JVM. The JVM target is only used to run the
 * (common) unit tests quickly, without emulator/simulator: `./gradlew jvmTest`.
 */
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun version(alias: String): String = libs.findVersion(alias).get().requiredVersion

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    applyDefaultHierarchyTemplate()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.uuid.ExperimentalUuidApi")
                if (name.startsWith("ios")) optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        commonMain.dependencies {
            implementation(libs.findLibrary("kotlinx-coroutines-core").get())
        }
        commonTest.dependencies {
            implementation(libs.findLibrary("kotlin-test").get())
            implementation(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}

android {
    namespace = "com.beatgridmedia." + project.name.replace("-", ".")
    compileSdk = version("android-compile-sdk").toInt()
    defaultConfig {
        minSdk = version("android-min-sdk").toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
