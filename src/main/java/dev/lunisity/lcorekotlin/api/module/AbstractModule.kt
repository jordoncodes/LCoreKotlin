package dev.lunisity.lcorekotlin.api.module

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.api.interfaces.Loadable

abstract class AbstractModule {

    abstract var loadables: MutableList<Loadable>

    abstract var enabled: Boolean

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