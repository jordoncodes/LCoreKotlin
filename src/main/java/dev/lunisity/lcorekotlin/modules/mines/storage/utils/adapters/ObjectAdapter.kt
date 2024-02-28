package dev.lunisity.lcorekotlin.modules.mines.storage.utils.adapters

import com.google.gson.*
import java.lang.reflect.Type


abstract class ObjectAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {

    abstract override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): T

    abstract override fun serialize(`object`: T, type: Type?, context: JsonSerializationContext?): JsonElement?

}