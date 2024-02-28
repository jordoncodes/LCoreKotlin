package dev.lunisity.lcorekotlin.api.commands

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class CommandContext(var sender: CommandSender, var arguments: Array<String>) {

    companion object {
        var sender: CommandSender? = null
        var arguments: Array<String>? = null
    }

    fun getLength(): Int {
        return arguments.size
    }

    fun get(index: Int): String {
        return arguments[index]
    }

    fun asInt(index: Int): Int {
        return try {
            arguments[index].toInt()
        } catch (exception: NumberFormatException) {
            Int.MAX_VALUE
        }
    }

    fun asDouble(index: Int): Double {
        return try {
            arguments[index].toDouble()
        } catch (exception: NumberFormatException) {
            Double.MAX_VALUE
        }
    }

    fun asLong(index: Int): Long {
        return try {
            arguments[index].toLong()
        } catch (exception: NumberFormatException) {
            Long.MAX_VALUE
        }
    }

    fun asPlayer(index: Int): Player? {
        return Bukkit.getPlayer(arguments[index])
    }

    fun asOfflinePlayer(index: Int): OfflinePlayer? {
        return Bukkit.getOfflinePlayer(arguments[index])
    }

    fun isInt(index: Int): Boolean {
        return asInt(index) != Int.MAX_VALUE
    }

    fun isDouble(index: Int): Boolean {
        return asDouble(index) != Double.MAX_VALUE
    }

    fun isLong(index: Int): Boolean {
        return asLong(index) != Long.MAX_VALUE
    }

    fun isPlayer(index: Int): Boolean {
        return asPlayer(index) != null
    }

    fun isOfflinePlayer(index: Int): Boolean {
        return asOfflinePlayer(index) != null
    }

    inline fun <reified T : Enum<T>> getEnum(index: Int): T? {
        return try {
            enumValueOf<T>(arguments[index].uppercase(Locale.getDefault()))
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T : Enum<T>> isEnum(index: Int): Boolean {
        return getEnum<T>(index) != null
    }
}