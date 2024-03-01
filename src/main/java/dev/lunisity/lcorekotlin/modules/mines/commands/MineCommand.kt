package dev.lunisity.lcorekotlin.modules.mines.commands

import AbstractCommand
import dev.lunisity.lcorekotlin.api.commands.CommandContext
import dev.lunisity.lcorekotlin.modules.mines.MinesModule
import dev.lunisity.lcorekotlin.modules.mines.commands.impl.GiveCommand
import dev.lunisity.lcorekotlin.modules.mines.commands.impl.TeleportCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager

class MineCommand(manager: MinesManager) : AbstractCommand<MinesModule>() {

    init {
        initializeAliases("mine", "mines", "pmine")

        withSubCommand(GiveCommand(manager), TeleportCommand(manager))
    }

    override fun execute(context: CommandContext?) {

    }
}