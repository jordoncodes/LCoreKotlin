package dev.lunisity.lcorekotlin.modules.mines.storage.utils.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import java.lang.reflect.Type

class BukkitLocationAdapter : ObjectAdapter<Location>() {
    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): Location {

        var jsonObject: JsonObject = element.asJsonObject

        var worldName: String = jsonObject.get("world").asString
        var world: World? = Bukkit.getWorld(worldName)

        var x: Double = jsonObject.get("x").asDouble
        var y: Double = jsonObject.get("y").asDouble
        var z: Double = jsonObject.get("z").asDouble
        var yaw: Float = jsonObject.get("yaw").asFloat
        var pitch: Float = jsonObject.get("pitch").asFloat

        return Location(world, x, y, z, yaw, pitch)

    }

    override fun serialize(`object`: Location, type: Type?, context: JsonSerializationContext?): JsonElement? {
        var jsonObject: JsonObject = JsonObject()

        jsonObject.addProperty("world", `object`.world?.name)
        jsonObject.addProperty("x", `object`.x)
        jsonObject.addProperty("y", `object`.y)
        jsonObject.addProperty("z", `object`.z)

        var yaw: Float = (`object`.yaw)
        var pitch: Float = (`object`.pitch)

        if (yaw > 0.0F) {
            jsonObject.addProperty("yaw", `object`.yaw)
        }

        if (pitch > 0.0F) {
            jsonObject.addProperty("pitch", `object`.pitch)
        }
        return jsonObject
    }
}