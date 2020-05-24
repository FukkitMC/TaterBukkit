rootProject.name = "craftbukkit"



pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }

        maven {
            name = "Fukkit"
            url = uri("E:\\dev\\fabric\\fukkit-repo")
        }
    }
}
