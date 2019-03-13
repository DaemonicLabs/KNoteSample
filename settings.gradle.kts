pluginManagement {
    repositories {
        maven(url = "http://maven.modmuss50.me/") {
            name = "modmuss50"
        }
        maven(url = "https://kotlin.bintray.com/kotlinx/") {
            name = "kotlinx"
        }
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
rootProject.name = "sample-notebook"
