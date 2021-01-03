package org.freeplanefx.application

import kotlin.test.*
import io.kotest.property.*
import org.mockito.*

class FreeplaneFxPresenterTest {

    @Test
    fun testInitialization() {
        val userInterface = Mockito.mock(UserInterface::class.java)

        FreeplaneFxPresenter(userInterface)

        Mockito.verify(userInterface).initialize()
    }
}