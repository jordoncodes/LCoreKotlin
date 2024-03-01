@file:Suppress("LateinitVarOverridesLateinitVar")

package dev.lunisity.lcorekotlin.modules.mines

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.api.interfaces.Loadable
import dev.lunisity.lcorekotlin.api.module.AbstractModule
import dev.lunisity.lcorekotlin.modules.mines.commands.MineCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.storage.load.MineLoader
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver
import kotlin.properties.Delegates

class MinesModule(
    private val manager: MinesManager,
    private val mineSaver: MineSaver,
    private val core: LCore
) : AbstractModule() {

    override fun enable() {
        this.enabled = true

        core.logger.info("MinesModule has enabled.")
        manager.enable()

        mineSaver.setupMinesDirectory()

        register(MineCommand(manager, core))
    }

    override fun disable() {
        this.enabled = false

        for (loadable in loadables) {
            loadable.unregister(this)
        }

        manager.disable()
        core.logger.info("MinesModule has disabled.")
    }

    override fun name() : String {
        return "MinesModule"
    }
}