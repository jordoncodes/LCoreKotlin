package dev.lunisity.lcorekotlin.modules.mines.commands.impl

import dev.lunisity.lcorekotlin.api.commands.CommandContext
import dev.lunisity.lcorekotlin.api.commands.SubCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import org.bukkit.entity.Player

class GiveCommand(private var manager: MinesManager) : SubCommand() {

    init {
        initializeAliases("give")
    }

    override fun execute(context: CommandContext) {
        val player:Player = context.sender as Player

        manager.createMine(player)
    }
}