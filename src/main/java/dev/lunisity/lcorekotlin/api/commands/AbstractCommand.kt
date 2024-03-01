import dev.lunisity.lcorekotlin.api.commands.CommandContext
import dev.lunisity.lcorekotlin.api.commands.SubCommand
import dev.lunisity.lcorekotlin.api.interfaces.Loadable
import dev.lunisity.lcorekotlin.api.module.AbstractModule
import dev.lunisity.lcorekotlin.commons.text.TextReplacer
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Predicate

abstract class AbstractCommand<T : AbstractModule?>(vararg aliases: String?) : Command(
    aliases[0]!!
),
    Loadable {
    private val subCommands: MutableList<SubCommand?> = ArrayList<SubCommand?>()

    init {
        initializeAliases(*aliases)
    }

    fun initializeAliases(vararg aliases: String?) {
        if (aliases.size == 1) {
            return
        }
        setAliases(listOf(*Arrays.copyOfRange(aliases, 1, aliases.size)))
    }

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val context: CommandContext = CommandContext(sender, args)

        if (args.isEmpty() || subCommands.isEmpty()) {
            this.execute(context)
            return true
        }

        val subCommand: SubCommand? =
            subCommands.stream().filter { command: SubCommand? ->
                command?.aliases
                    ?.contains(args[0]) ?: false
            }.findAny().orElse(null)

        if (subCommand == null) {
            invalidSubCommand(sender, args[0])
            return false
        }
        subCommand.execute(context)
        return true
    }

    private fun invalidSubCommand(sender: CommandSender, argument: String) {
        val messages = TextReplacer()
            .with("%sub-command%", argument)
            .apply(INVALID_COMMAND, INVALID_COMMAND_SECONDARY)
            .toList()  // Convert array of strings to list of strings

        sender.sendMessage(*messages.toTypedArray())  // Spread operator (*) to pass the list as varargs

        if (sender !is Player) {
            return
        }
        sender.playSound(sender.location, Sound.BLOCK_ANVIL_BREAK, 1.0f, 1.0f)
    }

    fun withSubCommand(vararg subCommand: SubCommand) {
        subCommands.addAll(subCommand.toList())
    }

    protected abstract fun execute(context: CommandContext?)

    override fun register(module: AbstractModule) {
        val method = Bukkit.getServer().javaClass.getMethod("getCommandMap")
        method.isAccessible = true

        val commandMap = method.invoke(Bukkit.getServer()) as CommandMap
        method.isAccessible = false

        commandMap.register(module.name().lowercase(Locale.getDefault()), this)
    }

    override fun unregister(module: AbstractModule) {
        val commands = knownCommands
        val name = module.name().lowercase(Locale.getDefault())

        for (alias in aliases) {
            commands.remove("$name:$alias")
            commands.remove(alias)
        }

        val method = Bukkit.getServer().javaClass.getMethod("syncCommands")
        method.isAccessible = true

        method.invoke(Bukkit.getServer())
        method.isAccessible = false
    }

    private val knownCommands: MutableMap<String, Command>
        get() {
            val method = Bukkit.getServer().javaClass.getMethod("getCommandMap")
            method.isAccessible = true

            val commandMap = method.invoke(Bukkit.getServer()) as CommandMap
            method.isAccessible = false
            val method1 = commandMap.javaClass.getMethod("getKnownCommands")

            val commands = method1.invoke(commandMap) as MutableMap<String, Command>

            method1.isAccessible = false
            return commands
        }

    companion object {
        private const val INVALID_COMMAND = "&7[!] &cThe sub command &4%sub-command% &cisn't registered."
        private const val INVALID_COMMAND_SECONDARY = "&c&oPlease try again with another argument."
    }
}
