plugins {
    kotlin("multiplatform") version "2.4.21"
    kotlin("plugin.serialization") version "2.4.21"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
}

group = "com.visma.guardian"
version = "0.1.0"

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    linuxX64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-core:3.6.1")
            implementation("io.ktor:ktor-client-content-negotiation:3.6.1")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.6.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.13.0")
            implementation("com.squareup.okio:okio:3.18.2")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("io.ktor:ktor-client-mock:3.6.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
        }
        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-cio:3.6.1")
        }
        linuxX64Main.dependencies {
            implementation("io.ktor:ktor-client-curl:3.6.1")
        }
        macosArm64Main.dependencies {
            implementation("io.ktor:ktor-client-darwin:3.6.1")
        }
    }
}
