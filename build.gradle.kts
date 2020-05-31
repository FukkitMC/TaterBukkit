plugins {
    id("fabric-loom") version "g0.4.0-SNAPSHOT"
    id("com.peterabeles.gversion") version "1.2.4"
}

gversion {
    srcDir = ("src/main/java/")
    classPackage = ("io.github.fukkitmc.fukkit")
    className = ("GitVersion")
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
}

dependencies {
    "ecj"("org.eclipse.jdt:ecj:3.21.0")
    minecraft("net.minecraft", "minecraft", "1.15.2")
    mappings("net.fabricmc", "yarn", "1.15.2+build.15", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.8.2+build.194")
    compileOnly("com.google.code.findbugs", "jsr305", "3.0.2")

    implementation("com.google.code.gson:gson:2.8.0")
    implementation("com.google.guava:guava:21.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("commons-lang:commons-lang:2.6")
    implementation("jline:jline:2.12.1")
    implementation("mysql:mysql-connector-java:5.1.48")
    implementation("org.bukkit:bukkit:1.15.2-R0.1-SNAPSHOT")
    implementation("org.xerial:sqlite-jdbc:3.30.1")
    implementation("org.yaml:snakeyaml:1.25")
    include("com.googlecode.json-simple:json-simple:1.1.1")
    include("commons-lang:commons-lang:2.6")
    include("jline:jline:2.12.1")
    include("mysql:mysql-connector-java:5.1.48")
    include("org.bukkit:bukkit:1.15.2-R0.1-SNAPSHOT")
    include("org.xerial:sqlite-jdbc:3.30.1")
    include("org.yaml:snakeyaml:1.25")
    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
}

tasks.withType<JavaCompile> {
    options.isFork = true

    options.forkOptions.apply {
        executable = "java"
        jvmArgs = listOf("-classpath", project.configurations["ecj"].asPath, "org.eclipse.jdt.internal.compiler.batch.Main", "-nowarn", "-g", "-verbose", "-referenceInfo")
    }
}
