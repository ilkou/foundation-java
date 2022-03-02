plugins {
    idea
}

ext["caffeine.version"] = "2.9.3"

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.soffa.gradle:gradle-plugin:2.2.5")
    }
}


apply(plugin = "soffa.sonatype-legacy-publish")

allprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = "soffa.java8")
}


tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    setForkEvery(100)
    reports.html.required.set(false)
    reports.junitXml.required.set(false)
}

tasks.withType<JavaCompile>().configureEach {
    options.isFork = true
}
