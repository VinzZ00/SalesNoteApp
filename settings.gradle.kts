pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // ChargeMap Repository
        maven("https://maven.chargemap.com/repository/chargemap-public/")

        // Github Repository
        maven("https://jitpack.io")


    }
}

rootProject.name = "SalesApp"
include(":app")
 