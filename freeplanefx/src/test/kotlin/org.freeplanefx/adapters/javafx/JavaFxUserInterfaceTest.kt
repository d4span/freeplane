package org.freeplanefx.adapters.javafx

import org.freeplanefx.application.*

import kotlin.test.*
import io.kotest.property.*
import org.mockito.*
import java.io.*

class JavaFxUserInterfaceTest {
    @Test
    fun isUserInterface() {
        assert(JavaFxUserInterface is UserInterface)
    }

    @Test
    fun initializationOfJavaFx() {
        JavaFxUserInterface.initialize()

        try {
            javafx.application.Platform.startup({})
            fail("JavaFX should already be initialized.");
        } catch (e: IllegalStateException){
            assert(true)
        }
    }
}