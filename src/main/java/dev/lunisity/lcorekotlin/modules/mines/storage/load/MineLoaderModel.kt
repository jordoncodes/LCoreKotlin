package dev.lunisity.lcorekotlin.modules.mines.storage.load

import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import java.io.File

interface MineLoaderModel {

    fun load(file: File) : PrivateMine?

}