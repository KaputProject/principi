import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
//    alias(libs.plugins.kotlin.jvm)("jvm")

    //Compose stuff
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "1.9.0" // Must match the Kotlin version

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
    // 🖥️ UI: JetBrains Compose za Desktop
    implementation(compose.desktop.currentOs)
//    implementation ("androidx.compose.material:material-icons-extended:1.6.1"

    // 🧪 Testiranje
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 📄 Delo z PDF datotekami
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    // 📢 Logging (SLF4J + Ktor)
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.ktor:ktor-client-logging:2.3.4") // 🔍 Ktor logging

    // 🌱 Environment spremenljivke iz .env datoteke
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")

    // 🌐 HTTP klient (OkHttp)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 🔄 JSON parserji
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20240303")

    // 🛢️ MongoDB driver
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")

    // 🔌 Ktor server
    implementation("io.ktor:ktor-server-netty:2.3.4")
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")

    // 🔄 Ktor klient in serializacija
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    // 🧵 Korutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

    // 📦 Kotlinx JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

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