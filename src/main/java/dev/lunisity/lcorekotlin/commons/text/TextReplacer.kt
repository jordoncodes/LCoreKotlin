package dev.lunisity.lcorekotlin.commons.text

import dev.lunisity.lcorekotlin.commons.interfaces.StringMappable
import dev.lunisity.lcorekotlin.commons.text.translate
import java.util.concurrent.TimeUnit

class TextReplacer {

    private val placeholders = mutableMapOf<String, String>()

    companion object {
        private val MAPPERS = mutableMapOf<Class<*>, StringMappable<*>>()

        fun <T> registerMapper(clazz: Class<T>, mapper: StringMappable<T>) {
            MAPPERS[clazz] = mapper
        }
    }

    fun with(placeholder: String, replacement: String): TextReplacer {
        placeholders[placeholder] = replacement
        return this
    }

    fun with(placeholder: String, supplier: () -> String): TextReplacer {
        return with(placeholder, supplier())
    }

    fun with(placeholder: String, `object`: Any): TextReplacer {
        val clazz = `object`.javaClass
        if (MAPPERS.containsKey(clazz)) {
            return with(placeholder, MAPPERS[clazz]!!)
        }
        return with(placeholder, `object`.toString())
    }

    fun withTimeFull(placeholder: String, time: Long, timeUnit: TimeUnit, timeFormatMode: TimeFormatMode): TextReplacer {
        return with(placeholder, timeFormatMode.apply(time, timeUnit))
    }

    fun withTime(placeholder: String, time: Long, timeUnit: TimeUnit, timeFormatMode: TimeFormatMode): TextReplacer {
        return with(placeholder, timeFormatMode.apply(time, timeUnit))
    }

    fun withTime(placeholder: String, time: Long, timeFormatMode: TimeFormatMode): TextReplacer {
        return with(placeholder, timeFormatMode.apply(time, TimeUnit.SECONDS))
    }

    fun apply(string: String?): String {
        requireNotNull(string) { "Cannot colorize a null string..." }
        if (string.isEmpty() || placeholders.isEmpty()) {
            return translate(string)
        }
        var result = string
        placeholders.forEach { (key, value) ->
            result = result!!.replace(key, value)
        }
        return translate(result!!)
    }

    fun apply(list: List<String>?): List<String> {
        return (list?.flatMap { listOf(apply(it)) } ?: emptyList()) as List<String>
    }

    fun apply(vararg string: String): Array<out String> {
        return string
    }
}