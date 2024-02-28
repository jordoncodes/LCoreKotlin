package dev.lunisity.lcorekotlin.modules.mines.storage.save

import dev.lunisity.lcorekotlin.LCore.Companion.core
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager.Companion.manager
import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import dev.lunisity.lcorekotlin.modules.mines.storage.utils.JsonUtils
import java.io.File
import java.io.IOException
import java.util.*

class MineSaver : MineSaverModel {

    companion object {
        lateinit var mineSaver: MineSaver
        lateinit var minesDirectory: File
    }

    init {
        mineSaver = this
    }

    override fun save(privateMine: PrivateMine, uuid: UUID) {

        var serialized: String = JsonUtils.GSON.toJson(privateMine)

        var directory: File = File(core.dataFolder.path + "/mines")
        var saveFilePath: String = "${uuid}_mines.json"

        try {
            File(saveFilePath).bufferedWriter().use { writer ->
                writer.write(serialized)
                core.logger.info("Private Mine saved to file: $saveFilePath")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun saveAll() {
        for (mine: PrivateMine in manager.getMines()){
            save(mine, mine.owner)
        }
    }

    fun setupMinesDirectory() {
        val directory: File = File(core.dataFolder.path + "/mines/")

        if (!directory.exists()){
            directory.mkdir()
        }
        minesDirectory = directory
    }
}