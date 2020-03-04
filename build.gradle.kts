import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
}

plugins {
    kotlin("multiplatform") version "1.3.61"
}

val configuration: String? = System.getenv("CONFIGURATION")
val executablePath: String? = System.getenv("EXECUTABLE_PATH")
val sdkName: String? = System.getenv("SDK_NAME")
val targetBuildDir: String? = System.getenv("TARGET_BUILD_DIR")

val buildType = configuration?.run {
    NativeBuildType.valueOf(toUpperCase())
} ?: NativeBuildType.DEBUG
val targetName = "ios"
val targetArch = if (sdkName?.startsWith("iphoneos") == true) "arm64" else "x64"

kotlin {

    targetFromPreset(presets.getByName<KotlinNativeTargetPreset>("ios${targetArch.capitalize()}"), targetName) {
        binaries.executable(listOf(buildType)) {
            baseName = "app"
        }
    }

    sourceSets.all {
        languageSettings.run {
            progressiveMode = true
            useExperimentalAnnotation("kotlin.Experimental")
        }
    }

}

dependencies {

    val ktor = "1.3.1"

    "commonMainApi"(kotlin("stdlib-common"))

    "commonMainApi"("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3}")
    "iosMainApi"("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.3")

    "commonMainApi"("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
    "iosMainApi"("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")

    "commonMainApi"("io.ktor:ktor-client-core:$ktor")
    "iosMainApi"("io.ktor:ktor-client-ios:$ktor")

    "commonMainApi"("io.ktor:ktor-client-serialization:$ktor")
    "iosMainApi"("io.ktor:ktor-client-serialization-native:$ktor")

    "commonMainApi"("io.ktor:ktor-client-json:$ktor")
    "iosMainApi"("io.ktor:ktor-client-json-native:$ktor")

    "commonMainApi"("io.ktor:ktor-client-logging:$ktor")
    "iosMainApi"("io.ktor:ktor-client-logging-native:$ktor")

}

val target = kotlin.targets[targetName] as KotlinNativeTarget
val kotlinBinary = target.binaries.getExecutable(buildType)

val xcodeTaskName = "xcode"
if (executablePath != null && sdkName != null && targetBuildDir != null) {
    tasks.create<Copy>(xcodeTaskName) {
        dependsOn(kotlinBinary.linkTask)
        group = "xcode"
        destinationDir = file(targetBuildDir)
        from(kotlinBinary.outputFile)
        rename { executablePath }
    }
} else {
    tasks.create(xcodeTaskName) {
        group = "xcode"
        doLast {
            throw IllegalStateException("Please run the '$xcodeTaskName' task from XCode")
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "6.2"
    distributionType = Wrapper.DistributionType.ALL
}
