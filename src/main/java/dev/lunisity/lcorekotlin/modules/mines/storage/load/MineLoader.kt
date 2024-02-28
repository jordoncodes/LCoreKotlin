package dev.lunisity.lcorekotlin.modules.mines.storage.load

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.modules.mines.MinesModule
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import dev.lunisity.lcorekotlin.modules.mines.storage.utils.JsonUtils
import java.io.File
import java.io.FileReader
import java.io.IOException

class MineLoader : MineLoaderModel {

    companion object {
        lateinit var mineLoader: MineLoader
    }
    init {
        mineLoader = this
    }

    override fun load(file: File): PrivateMine? {
        return try {
            FileReader(file).use { reader ->
                LCore.core.logger.info("Loaded mine from file: ${file.name}")
                JsonUtils.GSON.fromJson(reader, PrivateMine::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
          return null
        }
    }
}