plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":spec"))
    runtimeOnly(project(":prvaImpl"))
    runtimeOnly(project(":drugaImplm"))
    runtimeOnly(project(":trecaImpl"))
    runtimeOnly(project(":cetvrtaImpl"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.poi:poi-ooxml:5.2.3") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    }
    implementation("org.apache.poi:poi:5.2.3") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

application {
    mainClass.set("testApp.TestKt")
}


tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    mergeServiceFiles()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}