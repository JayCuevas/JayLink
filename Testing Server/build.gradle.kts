import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "com.jaydlc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    //    implementation(fileTree("../Android/lib/build/libs/") { include("*.jar") })
    implementation(project(":lib"))
    implementation("com.beust:klaxon:5.0.1")
    implementation("io.reactivex.rxjava3:rxjava:3.0.9")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}