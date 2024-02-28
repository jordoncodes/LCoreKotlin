package dev.lunisity.lcorekotlin.modules.mines.commands.impl

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.LCore.Companion.core
import dev.lunisity.lcorekotlin.api.commands.CommandContext
import dev.lunisity.lcorekotlin.api.commands.SubCommand
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager
import dev.lunisity.lcorekotlin.modules.mines.managers.MinesManager.Companion.manager
import org.bukkit.entity.Player

class TeleportCommand : SubCommand() {

    init {
        initializeAliases("teleport", "go", "tp")
    }

    override fun execute(context: CommandContext) {
        var player:Player = context.sender as Player
        var target:Player = core.server.getPlayer(context.arguments[0]) as Player

        if (player.hasPermission("mines.admin")){
            manager.adminTeleportMine(player, target)
        }

        manager.teleportMine(player)
    }
}