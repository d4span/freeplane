val archunitVersion = "0.15.0"
val springBootVersion = "2.4.0"

plugins {
    kotlin("jvm") version "1.4.21"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin{
    sourceSets["main"].apply {
        dependencies {
            implementation(project(":freeplane_framework"))
            implementation("org.springframework.boot:spring-boot-starter:" + springBootVersion)
        }
    }

    sourceSets["test"].apply {
        dependsOn(sourceSets["main"])

        dependencies {
            implementation(kotlin("test-junit5"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))

            implementation("org.springframework.boot:spring-boot-starter-test:" + springBootVersion)
            implementation("com.tngtech.archunit:archunit-junit5:" + archunitVersion)
        }
    }
}