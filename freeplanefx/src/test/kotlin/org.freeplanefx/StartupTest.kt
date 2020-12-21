package org.freeplanefx

import kotlin.test.assertNotNull
import kotlin.test.Test

import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.SpringApplication

@SpringBootTest
class StartupTest {

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun applicationContextLoads(){
        assertNotNull(context != null)
    }
}