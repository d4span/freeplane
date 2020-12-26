package org.freeplanefx

import kotlin.test.*

import com.tngtech.archunit.base.*
import com.tngtech.archunit.core.*
import com.tngtech.archunit.core.domain.*
import com.tngtech.archunit.core.importer.*
import com.tngtech.archunit.library.*
import com.tngtech.archunit.library.plantuml.*

import com.tngtech.archunit.core.importer.ImportOption.*
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.*
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.*

class ArchitectureTest {
    val inJdkOrKotlinStdLib = object: DescribedPredicate<JavaClass>("Classes not in JDK...") {
        override fun apply(javaClass: JavaClass): Boolean {
            val fullClassName = javaClass.fullName

            return fullClassName.startsWith("java.").or(fullClassName.startsWith("kotlin."))
        }
    }

    val onionArchitecture = Architectures.onionArchitecture()
        .domainModels("org.freeplanefx.model..")
        .domainServices("org.freeplanefx.domain..")
        .applicationServices("org.freeplanefx.application..")
        .adapter("Spring", "org.freeplanefx.adapters.spring..")
        .adapter("Freeplane", "org.freeplanefx.adapters.freeplane..")

    @Test
    fun overallArchitecture() {
        val diagram = ArchitectureTest::class.java.getResource("/architecture.puml")
        assertNotNull(diagram, "PlantUml diagram not found.")

        val classesOnClasspath = ClassFileImporter().withImportOption(DoNotIncludeTests()).importClasspath()

        val adhereToPlantUmlRule = classes().that().resideOutsideOfPackages("java..", "kotlin..")
            .should(adhereToPlantUmlDiagram(diagram, consideringAllDependencies())
            .ignoreDependenciesWithTarget(inJdkOrKotlinStdLib));

        val numClassesOnClasspath = classesOnClasspath.size
        assertNotEquals(0, numClassesOnClasspath, "No classes outside of JDK found on classpath...")

        onionArchitecture.check(classesOnClasspath)
        adhereToPlantUmlRule.check(classesOnClasspath)
    }
}