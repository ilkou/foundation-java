plugins {
    idea
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.soffa.gradle:soffa-gradle-plugin:2.3.0")
    }
}


apply(plugin = "soffa.sonatype-legacy-publish")

allprojects {
    apply(plugin = "soffa.default-repositories")
    apply(plugin = "soffa.java17")
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
