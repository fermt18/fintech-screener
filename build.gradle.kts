import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "me.u2y21863"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons", "commons-csv", "1.9.0")
    implementation("org.json", "json", "20220320")
    implementation("io.ktor:ktor-server-core:2.0.1")
    implementation("io.ktor:ktor-client-cio:2.0.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}