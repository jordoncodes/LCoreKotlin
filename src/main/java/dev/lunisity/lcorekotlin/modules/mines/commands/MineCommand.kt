package dev.lunisity.lcorekotlin.modules.mines.commands

import AbstractCommand
import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.api.commands.CommandContext
import dev.lunisity.lcorekotlin.modules.mines.MinesModule
import dev.lunisity.lcorekotlin.modules.mines.commands.impl.GiveCommand
import dev.lunisity.lcorekotlin.modules.mines.commands.impl.TeleportCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager

class MineCommand() : AbstractCommand<MinesModule>() {

    init {
        initializeAliases("mine", "mines", "pmine")

        withSubCommand(GiveCommand(), TeleportCommand())
    }

    override fun execute(context: CommandContext?) {

    }
}