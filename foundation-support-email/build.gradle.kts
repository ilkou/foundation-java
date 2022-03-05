plugins {
    id("foundation.java8")
    id("foundation.maven-publish")
    id("foundation.test.junit5")

}

dependencies {
    api(project(":foundation-commons"))
    implementation("org.apache.commons:commons-email:1.5")
    implementation("com.sendgrid:sendgrid-java:4.8.3")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:2.6.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.4")

}
repositories {
    mavenCentral()
}

