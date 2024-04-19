plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.springboot.library")
}

dependencies {
    api(project(":foundation-commons"))
    api("com.amazonaws:aws-java-sdk-s3:1.12.701")
    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3") // Don't use version 3, it's not compatible with Java8
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:3.2.4")
    testImplementation(project(":foundation-service-test"))
}
repositories {
    mavenCentral()
}

