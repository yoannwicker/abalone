plugins {
    id("java")
}

group = "com.app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<GradleBuild>("buildWithoutTests") {
    tasks = listOf("assemble")
}
