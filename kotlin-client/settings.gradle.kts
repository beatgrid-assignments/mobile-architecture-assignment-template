pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "measurementkit-client"

include(
    ":beatkit-foundation",
    ":beatkit-module",
    ":measurementkit-model",
    ":measurementkit-module",
    ":measurementkit-viewmodel",
)

// local.properties is not under version control and composite builds do not share it: point the Android SDK to
// ANDROID_HOME, or else to the default location of Android Studio, if it does not exist yet.
run {
    val localProperties = file("local.properties")
    val defaultSdk = File(System.getProperty("user.home"), "Library/Android/sdk")
    if (!localProperties.exists() && System.getenv("ANDROID_HOME") == null && defaultSdk.exists()) {
        localProperties.writeText("sdk.dir=${defaultSdk.absolutePath}\n")
    }
}
