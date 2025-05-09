pluginManagement {
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

rootProject.name = "Rooms"
include(":app")

include(":domain-onboarding")
include(":data-onboarding")
include(":ui-onboarding")

include(":domain-rooms")
include(":data-rooms")
include(":ui-rooms")

include(":domain-room")
include(":data-room")
include(":ui-room")

include(":domain-common-speedcubing")
include(":data-common-speedcubing")
include(":ui-common-speedcubing")

include(":domain-core")
include(":data-core")
include(":ui-core")
include(":network-core")
