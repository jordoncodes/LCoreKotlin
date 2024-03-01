package dev.lunisity.lcorekotlin

import dev.lunisity.lcorekotlin.api.module.AbstractModule
import dev.lunisity.lcorekotlin.modules.mines.MinesModule
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.storage.load.MineLoader
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class LCore : JavaPlugin () {

    companion object {
        var modules: LinkedHashMap<String, AbstractModule> = LinkedHashMap()

        private lateinit var core: LCore
        fun get() : LCore { return core }
    }

    private lateinit var minesManager: MinesManager
    private val mineSaver = MineSaver(this, minesManager)
    private val minesModule = MinesModule (minesManager, mineSaver, this)
    private val mineLoader = MineLoader(this)

    override fun onEnable(){
        minesManager = MinesManager(this, mineSaver, mineLoader)

        core = this

        logger.info("LCore enabled.")

        this.loadModules()
        this.initModules()
    }

    private fun loadModules(){
        this.loadModule(minesModule)
    }


    private fun initModules() {
        modules.put(minesModule.name().lowercase(Locale.getDefault()), minesModule)
    }


    private fun loadModule(module: AbstractModule) {
        if (module.enabled){
            return
        }
        module.enable()
        logger.info("Module ${module.name()} has been enabled.")
    }

    private fun unloadModule(module: AbstractModule) {
        if (!module.enabled){
            return
        }
        module.disable()
        logger.info("Module ${module.name()} has been disabled.")
    }

    private fun unloadModules() {
        for (module in modules.values) {
            this.unloadModule(module)
        }
    }

    override fun onDisable() {
        logger.info("LCore disabled.")
        this.unloadModules()
    }
}
