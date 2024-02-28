package dev.lunisity.lcorekotlin.modules.mines.storage.save

import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import java.util.*

interface MineSaverModel {

    fun save(privateMine: PrivateMine, uuid: UUID)

    fun saveAll()

}