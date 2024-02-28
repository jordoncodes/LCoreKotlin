package dev.lunisity.lcorekotlin.modules.mines

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.LCore.Companion.core
import dev.lunisity.lcorekotlin.api.interfaces.Loadable
import dev.lunisity.lcorekotlin.api.module.AbstractModule
import dev.lunisity.lcorekotlin.modules.mines.commands.MineCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager.Companion.manager
import dev.lunisity.lcorekotlin.modules.mines.storage.load.MineLoader
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver.Companion.mineSaver
import kotlin.properties.Delegates

class MinesModule(override var loadables: MutableList<Loadable>, override var enabled: Boolean) : AbstractModule() {

    companion object {
        lateinit var minesmodule: MinesModule
    }

    override fun enable() {
        core.logger.info("MinesModule has enabled.")

        minesmodule = this
        this.enabled = true

        manager = MinesManager(manager)
        manager.enable()

        mineSaver = MineSaver()
        mineSaver.setupMinesDirectory()

        register(MineCommand())
    }

    override fun disable() {
        this.enabled = false

        for (loadable in this.loadables) {
            loadable.unregister(this)
        }

        manager.disable()
        core.logger.info("MinesModule has disabled.")
    }

    override fun name() : String {
        return "MinesModule"
    }
}