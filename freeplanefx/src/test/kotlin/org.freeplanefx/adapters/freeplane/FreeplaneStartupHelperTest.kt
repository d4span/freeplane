package org.freeplanefx.adapters.freeplane

import org.freeplane.main.application.*
import kotlin.test.*
import io.kotest.property.*
import org.mockito.*
import java.io.*

typealias ResourceController = ApplicationResourceController

class FreeplaneStartupHelperTest {
    @Test
    fun testUserPropertiesExist() {
        testIsFirstStart(true)
    }

    @Test
    fun testUserPropertiesDoNotExist() {
        testIsFirstStart(false)
    }

    fun testIsFirstStart(prefsFileExists: Boolean) {
        val userPrefsFile = Mockito.mock(File::class.java)
        Mockito.`when`(userPrefsFile.exists()).thenReturn(prefsFileExists)

        val staticMock = Mockito.mockStatic(ResourceController::class.java)
        staticMock.use {
            staticMock.`when`<File>(ResourceController::getUserPreferencesFile).thenReturn(userPrefsFile)

            assertEquals(prefsFileExists, FreeplaneStartupHelper.isFirstStart)

            Mockito.verify(userPrefsFile).exists()
            Mockito.verifyNoMoreInteractions(userPrefsFile)
        }
    }
}