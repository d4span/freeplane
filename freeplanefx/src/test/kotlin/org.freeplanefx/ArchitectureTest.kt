package org.freeplanefx

import kotlin.test.*

import com.tngtech.archunit.base.*
import com.tngtech.archunit.core.*
import com.tngtech.archunit.core.domain.*
import com.tngtech.archunit.core.importer.*
import com.tngtech.archunit.library.plantuml.*

import com.tngtech.archunit.core.importer.ImportOption.*
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.*
import com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.*

class ArchitectureTest {
    val areNotPartOfJdk = object: DescribedPredicate<JavaClass>("Classes not in JDK...") {
        override fun apply(javaClass: JavaClass) = javaClass.packageName.contains("java.").not()
    }

    @Test
    fun overallArchitecture() {
        val diagram = ArchitectureTest::class.java.getResource("/architecture.puml")

        val classesOnClasspath = ClassFileImporter().withImportOption(DoNotIncludeTests()).importClasspath()
        val architectureRule = classes().that(areNotPartOfJdk).should(adhereToPlantUmlDiagram(diagram, consideringAllDependencies()))

        assertNotEquals(0, classesOnClasspath.size, "No classes outside of JDK found on classpath...")

        architectureRule.check(classesOnClasspath)
    }
}