package dev.lunisity.lcorekotlin.modules.mines.storage.save

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import dev.lunisity.lcorekotlin.modules.mines.storage.utils.JsonUtils
import java.io.File
import java.io.IOException
import java.util.*

class MineSaver(
    private val core: LCore,
    private val manager: MinesManager
) : MineSaverModel {

    companion object {
        lateinit var minesDirectory: File
    }

    override fun save(privateMine: PrivateMine, uuid: UUID) {

        val serialized: String = JsonUtils.GSON.toJson(privateMine)

        val saveFilePath = File(minesDirectory, "$uuid.json")

        try {
            saveFilePath.bufferedWriter().use { writer ->
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
        val directory = File(core.dataFolder.path + "/mines/")

        if (!directory.exists()){
            directory.mkdir()
        }
        minesDirectory = directory
    }
}