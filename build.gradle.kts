import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
//    alias(libs.plugins.kotlin.jvm)("jvm")

    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}
dependencies {
    implementation(compose.desktop.currentOs)
    implementation("io.github.serpro69:kotlin-faker:2.0.0-rc.10")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.ktor:ktor-client-logging:2.3.4")

    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20240303")

    implementation("org.mongodb:mongodb-driver-sync:4.11.0")

    implementation("io.ktor:ktor-server-netty:2.3.4")
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")

    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

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
    useJUnitPlatform()
}
