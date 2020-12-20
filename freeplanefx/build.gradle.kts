val archunitVersion = "0.15.0"

plugins {
    kotlin("jvm") version "1.4.21"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin{
    sourceSets["main"].apply {
        dependencies {
            implementation(project(":freeplane_framework"))
        }
    }

    sourceSets["test"].apply {
        dependencies {
            implementation(kotlin("test-junit"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))

            implementation("com.tngtech.archunit:archunit:" + archunitVersion)
        }
    }
}