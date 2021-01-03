package org.freeplanefx.adapters.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

import org.springframework.context.annotation.*

import org.freeplanefx.application.*

@SpringBootApplication
open class FreeplaneFxApplication {

    @Bean
    open fun freeplanefxPresenter() = FreeplaneFxPresenter(object : UserInterface {
        override fun initialize() {}
    })
}