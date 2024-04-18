plugins {
    id("soffa.lombok")
    id("soffa.springboot.library")
    id("soffa.maven-publish")
}

dependencies {
    compileOnly(project(":foundation-application"))
    api(project(":foundation-commons"))
    api("com.intuit.karate:karate-junit5:1.4.1")

    api("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "com.vaadin.external.google")
    }

    compileOnly("org.springframework.boot:spring-boot-starter-web")
    api("com.h2database:h2:2.2.224")
    api("com.google.guava:guava:33.1.0-jre")
    api("commons-io:commons-io:2.16.1")
    api("org.awaitility:awaitility:4.2.1")
    //api("com.github.javafaker:javafaker:1.0.2")
    api("net.datafaker:datafaker:2.1.0")

    // implementation("org.mock-server:mockserver-netty:5.11.2")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
}

