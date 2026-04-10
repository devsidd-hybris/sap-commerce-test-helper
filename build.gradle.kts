plugins {
    id("org.jetbrains.intellij") version "1.17.2"
    kotlin("jvm") version "1.7.22"
}

group = "com.example.hybris"
version = "1.0.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2022.1")
    type.set("IC")
    plugins.addAll("java", "junit")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.patchPluginXml {
    sinceBuild.set("221")
    untilBuild.set("261.*")
}