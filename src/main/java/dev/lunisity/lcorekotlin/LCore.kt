package dev.lunisity.lcorekotlin;

import dev.lunisity.lcorekotlin.api.module.AbstractModule
import dev.lunisity.lcorekotlin.modules.mines.MinesModule
import dev.lunisity.lcorekotlin.modules.mines.MinesModule.Companion.minesmodule
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*
import kotlin.collections.LinkedHashMap

class LCore() : JavaPlugin () {

    companion object {

        lateinit var core: LCore

        var modules: LinkedHashMap<String, AbstractModule> = LinkedHashMap()

        fun getModule(name: String): AbstractModule? {
            return this.modules[name.lowercase(Locale.getDefault())]
        }

    }

    override fun onEnable(){
        logger.info("LCore enabled.")
        core = this;

        this.initModules()
        this.loadModules()
    }

    private fun loadModules(){
        this.loadModule(minesmodule);
    }


    private fun initModules() {
        minesmodule = MinesModule(LinkedList(), true)

        modules.put(minesmodule.name().lowercase(Locale.getDefault()), minesmodule);
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
        this.logger.info("LCore disabled.")
        this.unloadModules()
    }
}
