import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    application
    checkstyle
    jacoco
    java
    id("io.freefair.lombok") version "8.3"
    id("com.github.ben-manes.versions") version "0.47.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("hexlet.code.App")
}

jacoco {
    toolVersion = "0.8.9"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.9")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.h2database:h2:2.2.222")
    implementation("org.postgresql:postgresql:42.5.4")

    implementation("io.javalin:javalin:5.6.2")
    implementation("io.javalin:javalin-bundle:5.6.2")
    implementation("io.javalin:javalin-rendering:5.6.2")
    implementation("gg.jte:jte:3.1.0")
    implementation("com.konghq:unirest-java:4.0.0-RC2")
    implementation("org.jsoup:jsoup:1.16.1")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("failed", "skipped", "passed")
        exceptionFormat = TestExceptionFormat.FULL
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/test")
    }
}
