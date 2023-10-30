pluginManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        repositories {
            maven {
                // Use github hosted maven repo for now.
                // Will be uploaded to maven central later.
                url = uri("https://maven.wysaid.org/")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        repositories {
            maven {
                // Use github hosted maven repo for now.
                // Will be uploaded to maven central later.
                url = uri("https://maven.wysaid.org/")
            }
            maven {
                url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            }
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
}

rootProject.name = "CollageMaker"
include(":app")
 