plugins {
    id("soffa.java17")
    id("soffa.lombok")
    id("soffa.springboot")
    id("soffa.qa.coverage.l1")
    id("soffa.qa.pmd")
}

dependencies {
    implementation(project(":foundation-service"))
    implementation(project(":foundation-support-pubsub"))
    implementation(project(":foundation-support-email"))
    testImplementation(project(":foundation-service-test"))
}
