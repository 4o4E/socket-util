import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
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

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}