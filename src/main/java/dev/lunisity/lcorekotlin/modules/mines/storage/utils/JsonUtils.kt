package dev.lunisity.lcorekotlin.modules.mines.storage.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.lunisity.lcorekotlin.modules.mines.storage.utils.adapters.BukkitLocationAdapter
import dev.lunisity.lcorekotlin.modules.mines.storage.utils.adapters.BukkitWorldAdapter
import org.bukkit.Location
import org.bukkit.World



class JsonUtils {

    companion object {
        val GSON: Gson = builder().create()

        fun builder(): GsonBuilder {

            return GsonBuilder()
                .registerTypeAdapter(Location::class.java, BukkitLocationAdapter())
                .registerTypeAdapter(World::class.java, BukkitWorldAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls().setPrettyPrinting()

        }
    }
}