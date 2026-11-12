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
