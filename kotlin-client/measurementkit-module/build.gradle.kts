plugins {
    id("beatkit.project")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":beatkit-foundation"))
            api(project(":beatkit-module"))
            api(project(":measurementkit-model"))
        }
        jvmTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
