import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
//    alias(libs.plugins.kotlin.jvm)("jvm")

    //Compose stuff
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    // Apply the application plugin to add support for building a CLI application in Java.
//    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

//    // Use the JUnit 5 integration.
//    testImplementation(libs.junit.jupiter.engine)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    // This dependency is used by the application.
//    implementation(libs.guava)
//





// guccii
    // Used for dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    // Both used for Google API
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20240303")

}

//application {
//    // Define the main class for the application.
//    mainClass = "main"
//}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
compose.desktop {
    application {
        mainClass = "MainUIKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PrincipiCompose"
            packageVersion = "1.0.0"
        }
    }
}
tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}