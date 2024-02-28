package dev.lunisity.lcorekotlin.modules.mines.storage.utils.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.bukkit.Bukkit
import org.bukkit.World
import java.lang.reflect.Type

class BukkitWorldAdapter : ObjectAdapter<World>() {

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): World {
        val jsonObject: JsonObject = element.asJsonObject
        val worldName: String = jsonObject.get("name").asString

        return Bukkit.getServer().getWorld(worldName)!!
    }

    override fun serialize(`object`: World, type: Type?, context: JsonSerializationContext?): JsonElement? {
        val jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("name", `object`.name)
        return jsonObject
    }


}