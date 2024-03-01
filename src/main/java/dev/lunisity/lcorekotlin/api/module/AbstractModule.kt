package dev.lunisity.lcorekotlin.api.module

import dev.lunisity.lcorekotlin.api.interfaces.Loadable
import kotlin.properties.Delegates

abstract class AbstractModule {

    open lateinit var loadables: MutableList<Loadable>

    open var enabled by Delegates.notNull<Boolean>()

    companion object {
        lateinit var instance: AbstractModule
    }

    abstract fun enable()

    abstract fun disable()

    abstract fun name(): String

    fun register(loadable: Loadable) {
        loadable.register(instance)
        loadables.add(loadable)
    }

}