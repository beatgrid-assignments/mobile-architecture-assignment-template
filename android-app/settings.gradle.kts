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
    versionCatalogs {
        create("libs") {
            from(files("../kotlin-client/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "measurement-app"

// The Kotlin Multiplatform client is consumed as a composite build: the dependencies on
// `com.beatgridmedia.measurementkit:*` are substituted by the projects in ../kotlin-client.
includeBuild("../kotlin-client")

include(":app")
