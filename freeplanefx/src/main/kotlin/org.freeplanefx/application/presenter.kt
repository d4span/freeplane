package org.freeplanefx.application

interface UserInterface {
    fun initialize()
}

class FreeplaneFxPresenter (val userInterface: UserInterface) {
    init {
        userInterface.initialize()
    }
}