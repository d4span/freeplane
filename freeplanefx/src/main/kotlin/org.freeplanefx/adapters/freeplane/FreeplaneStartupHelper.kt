package org.freeplanefx.adapters.freeplane

import org.freeplane.main.application.*

interface FreeplaneStartupHelper {

    companion object {
        val isFirstStart: Boolean
            get() = ApplicationResourceController.getUserPreferencesFile().exists()
    }
}