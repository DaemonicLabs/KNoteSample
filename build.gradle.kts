import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("daemoniclabs.knote") version "1.0.0-SNAPSHOT"
    idea
}

knote {
    notebook("msleep") {
        dependencies {
            knote(group = "com.github.holgerbrandl", name = "krangl", version = "-SNAPSHOT")
        }
    }
    notebook("statistics") {
        dependencies {
            knote(group = "org.nield", name = "kotlin-statistics", version = "1.2.1")
        }
    }
    notebook("test_knote") {
        
    }
}

val wrapper = tasks.getByName<Wrapper>("wrapper") {
    gradleVersion = "5.2.1"
    distributionType = Wrapper.DistributionType.ALL
}

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            apiVersion = "1.3"
            languageVersion = "1.3"
            jvmTarget = "1.8"
        }
    }
}

repositories {
    mavenLocal()
    maven(url = "https://dl.bintray.com/kotlin/kotlinx-html/") {
        name = "Kotlinx"
    }
    mavenCentral()
    jcenter()
    maven(url = "https://jitpack.io" )
}

val ideaActive = System.getProperty("idea.active") == "true"

val hostRoot = rootDir.absoluteFile.parentFile

//if (ideaActive) {
//    val gradleWrapper = when {
//        Platform.isWindows -> "gradlew.bat"
//        Platform.isLinux -> "./gradlew"
//        Platform.isMac -> "./gradlew"
//        else -> throw IllegalStateException("unsupported OS: ${Platform.osType}")
//    }
//    val cmd = arrayOf(gradleWrapper, "publishToMavenLocal")
//    logger.lifecycle("executing ${cmd.joinToString(" ", "[", "]")} in $hostRoot")
//    val command = ProcessBuilder(*cmd)
//    val process = command
//        .directory(hostRoot)
//        .start()
//    val outStreamGobbler = Runnable {
//        process.inputStream.bufferedReader().use {
//            it.forEachLine { line -> logger.lifecycle("% $line") }
//        }
//    }
//    val errStreamGobbler = Runnable {
//        process.errorStream.bufferedReader().use {
//            it.forEachLine { line -> logger.error("% $line") }
//        }
//    }
//    Executors.newSingleThreadExecutor().submit(outStreamGobbler)
//    Executors.newSingleThreadExecutor().submit(errStreamGobbler)
//    val result = process.waitFor()
//    logger.lifecycle("command finished with code: $result")
//}

//val publishHost = task<GradleBuild>("publishHost") {
//    tasks = listOf("publishToMavenLocal")
//    dir = hostRoot
//    buildFile = hostRoot.resolve("build.gradle.kts")
//}
//
//val shadowCore = tasks.getByName<ShadowJar>("shadowCore") {
//    dependsOn += publishHost
//}
//val shadowViewer = tasks.getByName<ShadowJar>("shadowViewer") {
//    dependsOn += publishHost
//}

val notebookDir = rootDir.resolve("notebooks").apply { mkdirs() }

task<DefaultTask>("depsize") {
    group = "help"
    description = "prints dependency sizes"
    doLast {
        val formatStr = "%,10.2f"
        val size = configurations.default.get().resolve()
            .map { it.length() / (1024.0 * 1024.0) }.sum()

        val out = buildString {
            append("Total dependencies size:".padEnd(45))
            append("${String.format(formatStr, size)} Mb\n\n")
            configurations
                .default
                .get()
                .resolve()
                .sortedWith(compareBy { -it.length() })
                .forEach {
                    append(it.name.padEnd(45))
                    append("${String.format(formatStr, (it.length() / 1024.0))} kb\n")
                }
        }
        println(out)
    }
}
