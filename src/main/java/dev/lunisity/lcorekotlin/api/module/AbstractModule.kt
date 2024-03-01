package dev.lunisity.lcorekotlin.api.module

import dev.lunisity.lcorekotlin.api.interfaces.Loadable
import kotlin.properties.Delegates

abstract class AbstractModule {

    open lateinit var loadables: MutableList<Loadable>

    open var enabled = false

    abstract fun enable()

    abstract fun disable()

    abstract fun name(): String

    fun register(loadable: Loadable) {
        if (!(this::loadables.isInitialized)) {
            loadables = mutableListOf()
        }
        loadable.register(this)
        loadables.add(loadable)
    }

}