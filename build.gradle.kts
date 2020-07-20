plugins {
    id("fabric-loom") version "g0.4.0-SNAPSHOT"
}

group = "io.github.fukkitmc"
version = "1.0.0-SNAPSHOT"

configurations {
    create("ecj")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

minecraft {
    accessWidener = file("src/main/resources/craftbukkit.aw")

    loadDefinitions("definitions/definitions.json")
}

repositories {
    maven {
        name = "SpigotMC"
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }

    mavenLocal()
}

dependencies {
    "ecj"("org.eclipse.jdt:ecj:3.22.0")
    minecraft("net.minecraft", "minecraft", "1.16.1")
    mappings("net.fabricmc", "yarn", "1.16.1+build.21", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.9.0+build.204")
    compileOnly("com.google.code.findbugs", "jsr305", "3.0.2")

    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("jline:jline:2.12.1")
    implementation("mysql:mysql-connector-java:5.1.49")
    // Bukkit API is no longer being published to Spigot's Maven
    // For now, force users to have Bukkit
    implementation("org.bukkit:bukkit:1.16.1-R0.1-SNAPSHOT")
    implementation("org.xerial:sqlite-jdbc:3.32.3")

    include("com.googlecode.json-simple:json-simple:1.1.1")
    include("jline:jline:2.12.1")
    include("mysql:mysql-connector-java:5.1.49")
    include("org.bukkit:bukkit:1.16.1-R0.1-SNAPSHOT")
    include("org.xerial:sqlite-jdbc:3.32.3")
}

tasks.withType<JavaCompile> {
    options.headerOutputDirectory.convention(objects.directoryProperty())
    options.isFork = true

    options.forkOptions.apply {
        executable = "java"
        jvmArgs = listOf("-classpath", project.configurations["ecj"].asPath, "org.eclipse.jdt.internal.compiler.batch.Main", "-nowarn", "-g", "-verbose", "-referenceInfo")
    }
}
