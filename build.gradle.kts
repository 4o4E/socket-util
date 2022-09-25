import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    `maven-publish`
    `java-library`
}

group = Versions.group
version = Versions.version

repositories {
    mavenCentral()
}

dependencies {
    // gson
    implementation("com.google.code.gson:gson:2.9.0")
    // test
    testImplementation(kotlin("test", Versions.kotlin))
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

afterEvaluate {
    publishing.publications.create<MavenPublication>("java") {
        from(components["kotlin"])
        artifact(tasks.getByName("sourcesJar"))
        artifact(tasks.getByName("javadocJar"))
        artifactId = "socket-util"
        groupId = Versions.group
        version = Versions.version
    }
}