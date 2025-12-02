plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation("com.github.SlimifiedxD:bedrock:main-SNAPSHOT")
    implementation("org.slimecraft:funmands-paper:1.0-SNAPSHOT")
    annotationProcessor("com.github.SlimifiedxD:bedrock:main-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    runServer {
        minecraftVersion("1.21.10")
    }
}
