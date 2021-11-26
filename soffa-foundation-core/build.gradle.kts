plugins {
    id("soffa.java8")
    id("soffa.maven-publish")
    id("soffa.test.junit5")

}

dependencies {
    api(project(":soffa-foundation-api"))
    api(project(":soffa-foundation-models"))

    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    implementation("com.nimbusds:nimbus-jose-jwt:9.15.2")
    @Suppress("GradlePackageUpdate")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.github.michaelgantman:MgntUtils:1.5.1.2")
    api("com.jayway.jsonpath:json-path:2.6.0")
    api("javax.transaction:javax.transaction-api:1.3")
    api("org.hibernate.validator:hibernate-validator-annotation-processor:7.0.1.Final")
    api("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    api("org.checkerframework:checker-qual:3.18.1")
    api("com.amazonaws:aws-java-sdk-s3:1.12.111")
    implementation("com.auth0:java-jwt:3.18.2")
    api("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    api("org.jobrunr:jobrunr:4.0.1") {
        exclude(group = "com.zaxxer")
    }
    api("org.jdbi:jdbi3-core:3.23.0") {
        exclude(group = "com.github.ben-manes.caffeine")
    }
    api("org.liquibase:liquibase-core:4.6.1")
    api("com.h2database:h2:1.4.200")
    @Suppress("GradlePackageUpdate")
    api("commons-beanutils:commons-beanutils:1.9.4")
    implementation("org.apache.commons:commons-email:1.5")
    implementation("io.pebbletemplates:pebble:3.1.5")
}

