plugins {
    id("soffa.java17")
    id("soffa.lombok")
    id("soffa.springboot")
    // id("foundation.qa.coverage.l6")
}

dependencies {
    implementation(project(":foundation-service"))
    implementation(project(":foundation-support-email"))
    testImplementation(project(":foundation-service-test"))
}
