plugins {
    id("soffa.lombok")
    id("soffa.maven-publish")
    id("soffa.springboot.library")
    // id("foundation.qa.coverage.l4")
}

dependencies {
    api(project(":foundation-application"))
    implementation("io.nats:jnats:2.17.6")
    implementation("com.github.fridujo:rabbitmq-mock:1.2.0")
    implementation("org.springframework.kafka:spring-kafka")
    //implementation("org.apache.kafka:kafka_2.13:3.1.0")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    testImplementation(project(":foundation-service"))
    testImplementation(project(":foundation-service-test"))
    testImplementation("berlin.yuna:nats-streaming-server-embedded:0.25.4")

}
repositories {
    mavenCentral()
}

