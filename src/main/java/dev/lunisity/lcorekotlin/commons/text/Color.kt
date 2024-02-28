package dev.lunisity.lcorekotlin.commons.text

import org.bukkit.ChatColor

    fun translate(str: String): String {
        return ChatColor.translateAlternateColorCodes('&', str)
    }
