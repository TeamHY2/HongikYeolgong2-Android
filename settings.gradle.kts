pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HongikYeolgong2"
include(":app")
include(":app:auth")

include(":core:designsystem")

include(":calendar-domain")
include(":calendar-presentation")

include(":main-presentation")
include(":main-data")
include(":main-domain")

include(":setting-presentation")
include(":setting-data")
include(":setting-domain")

include(":timer-domain")
include(":timer-data")
include(":timer-presentation")

include(":onboarding-presentation")
include(":onboarding-data")
include(":onboarding-domain")

include(":ranking-presentation")
include(":ranking-domain")

include(":app:notification")
