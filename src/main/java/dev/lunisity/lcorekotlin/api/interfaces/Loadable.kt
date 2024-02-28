package dev.lunisity.lcorekotlin.api.interfaces

import dev.lunisity.lcorekotlin.api.module.AbstractModule

interface Loadable {

    fun register(module: AbstractModule)

    fun unregister(module: AbstractModule)

}