plugins {
    id("foundation.java8")
    id("foundation.springboot")
    id("foundation.qa.coverage.l1")
}

dependencies {
    implementation(project(":foundation-service"))
    implementation(project(":foundation-support-pubsub"))
    implementation(project(":foundation-support-email"))
    testImplementation(project(":foundation-service-test"))
}
