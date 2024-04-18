plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.test.junit5")
}

dependencies {
    api(project(":foundation-api"))
    api(project(":foundation-commons"))
    api("javax.inject:javax.inject:1")
    api("javax.transaction:javax.transaction-api:1.3")
    api("com.github.ben-manes.caffeine:caffeine:3.1.8")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:2.7.18")

}
repositories {
    mavenCentral()
}

