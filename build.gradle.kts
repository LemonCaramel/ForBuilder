plugins {
    id("java")
}

allprojects {
    group = "moe.caramel"
    version = property("projectVersion") as String
    description = "for Builder"
}

val targetJavaVersion = 16
java {
    val javaVersion: JavaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

dependencies {
    /* Bukkit API */
    compileOnly("moe.caramel", "daydream-api", property("ver_bukkit") as String)
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version, "description" to project.description)
        }
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(targetJavaVersion)
    }
}