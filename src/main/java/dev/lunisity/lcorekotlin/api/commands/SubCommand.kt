package dev.lunisity.lcorekotlin.api.commands

abstract class SubCommand(vararg aliases: String) {

    val aliases: List<String> = aliases.toList()

    init {
        initializeAliases(*aliases)
    }

    fun initializeAliases(vararg aliases: String?) {
        aliases.asList()
    }

    abstract fun execute(context: CommandContext)
}