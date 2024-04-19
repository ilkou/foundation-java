plugins {
    //id("soffa.java17")
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.test.junit5")
    // id("foundation.qa.coverage.l5")
}

dependencies {
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    api("org.checkerframework:checker-qual:3.42.0")
    api("org.checkerframework:checker:3.42.0")
    //api("org.checkerframework:jdk8:3.3.0")
    //api("javax.annotation:javax.annotation-api:1.3.2")
    api("jakarta.annotation:jakarta.annotation-api:3.0.0")
    api("io.swagger.core.v3:swagger-annotations:2.2.21")
    api("io.swagger.core.v3:swagger-models:2.2.21")
    api("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    api("commons-validator:commons-validator:1.8.0")

}

