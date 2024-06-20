plugins {
    id("soffa.java17")
    id("soffa.lombok")
    id("soffa.springboot")
    id("soffa.qa.coverage.l2")
    id("soffa.qa.pmd")
}

dependencies {
    implementation(project(":foundation-service"))
    implementation(project(":foundation-support-data"))
    testImplementation(project(":foundation-service-test"))
}
