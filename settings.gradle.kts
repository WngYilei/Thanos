pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        flatDir {
            dirs("./repo/")
        }
//        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}
rootProject.name = "Thanos"
include(":app")

include(":thanos-plugin")

