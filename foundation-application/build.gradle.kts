plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.test.junit5")
}

dependencies {
    api(project(":foundation-api"))
    api(project(":foundation-commons"))
    //api("jakarta.inject:jakarta.inject:1")
    api("jakarta.inject:jakarta.inject-api:2.0.1")
    api("jakarta.transaction:jakarta.transaction-api:2.0.1")
    api("com.github.ben-manes.caffeine:caffeine:3.1.8")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:3.2.4")

}
repositories {
    mavenCentral()
}

