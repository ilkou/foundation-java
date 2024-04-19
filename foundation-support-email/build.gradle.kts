plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.test.junit5")
}

dependencies {
    api(project(":foundation-commons"))
    implementation("org.apache.commons:commons-email:1.5")
    implementation("com.sendgrid:sendgrid-java:4.10.2")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:3.2.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
}
repositories {
    mavenCentral()
}

