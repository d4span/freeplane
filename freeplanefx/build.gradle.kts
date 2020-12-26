import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val archunitVersion = "0.15.0"
val springBootVersion = "2.4.0"
val mockitoVersion = "3.4.0"
val kotestVersion = "4.3.2"

plugins {
    kotlin("jvm") version "1.4.21"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions { 
        jvmTarget = "11"
    }
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
            implementation(project(":freeplane"))
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
            implementation("org.mockito:mockito-inline:" + mockitoVersion)
            implementation("io.kotest:kotest-property:" + kotestVersion)
        }
    }
}