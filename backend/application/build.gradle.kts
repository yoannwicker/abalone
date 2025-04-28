plugins {
    application
    id("java")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

application {
    mainClass.set("com.app.application.Application")
}

java.sourceCompatibility = JavaVersion.VERSION_21

val staticResources by configurations.creating {
    description = "Used to retrieve static resources needed by the jar"
}

tasks {
    bootJar {
        archiveBaseName.set("abalone")
    }

    jar {
        enabled = false
        manifest {
            attributes["Main-Class"] = "com.app.application.Application"
        }
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
    }
    test {
        useJUnitPlatform()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend:domain"))
    //runtimeOnly(project(":backend:infrastructure"))
    implementation(project(":backend:infrastructure"))
    //staticResources(project(":frontend", configuration = "angularDist"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-web")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    testImplementation("org.mockito:mockito-core:5.12.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
