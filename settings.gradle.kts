rootProject.name = "TaterBukkit"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }

        maven {
            name = "Fukkit 2"
            url = uri("../fukkit-repo")
        }

        maven {
            name = "Fukkit"
            url = uri("https://raw.githubusercontent.com/FukkitMC/fukkit-repo/master")
        }
    }
}
