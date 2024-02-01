plugins {
    id("foundation.java8")
    id("foundation.springboot")
    // id("foundation.qa.coverage.l2")
}

dependencies {
    implementation(project(":foundation-service"))
    implementation(project(":foundation-support-data"))
    testImplementation(project(":foundation-service-test"))
}
