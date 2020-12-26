package org.freeplanefx.adapters.spring

import kotlin.test.assertNotNull
import kotlin.test.Test
import kotlin.test.AfterTest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.SpringApplication

import org.mockito.*
import org.mockito.Mockito.*

import org.freeplane.main.application.ApplicationResourceController

import org.freeplanefx.application.*

typealias ResourceController = ApplicationResourceController

/**
 * Tests if the Spring application context loads properly.
 */
@SpringBootTest
class AppContextInitTest {

    @Autowired
    lateinit var context: ApplicationContext

    @Autowired
    lateinit var presenter: FreeplaneFxPresenter

    @Test
    fun applicationContextLoads(){
        assertNotNull(context)
        assertNotNull(presenter)
    }
}