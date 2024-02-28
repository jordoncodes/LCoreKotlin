package dev.lunisity.lcorekotlin.commons.world

import org.bukkit.*
import org.bukkit.generator.ChunkGenerator

class WorldManager {
    private val minesWorld: World = Bukkit.createWorld(
        WorldCreator("mines-world")
            .type(WorldType.FLAT)
            .generator(EmptyWorldGenerator())
    )!!
    private val defaultLocation: Location
    private val borderDistance: Int = 5000
    private var distance = 0
    private var direction: Direction? = null

    companion object {
        lateinit var worldManager: WorldManager
    }

    init {
        worldManager = this
        direction = Direction.NORTH
        defaultLocation = Location(minesWorld, 0.0, 0.0, 0.0)
    }

    fun getMinesWorld(): World {
        return minesWorld
    }

    @Synchronized
    fun nextFreeLocation(): Location {
        if (distance == 0) {
            distance++
            return defaultLocation.clone()
        }

        direction = direction ?: Direction.NORTH
        val loc = direction!!.addTo(defaultLocation, distance * borderDistance)
        direction = direction!!.next()
        if (direction == Direction.NORTH) distance++
        return loc
    }

    enum class Direction(private val xMulti: Int, private val zMulti: Int) {
        NORTH(0, -1), NORTH_EAST(1, -1),
        EAST(1, 0), SOUTH_EAST(1, 1),
        SOUTH(0, 1), SOUTH_WEST(-1, 1),
        WEST(-1, 0), NORTH_WEST(-1, -1);

        fun next(): Direction {
            return entries[(ordinal + 1) % entries.size]
        }

        fun addTo(loc: Location, value: Int): Location {
            return loc.clone().add(value * xMulti.toDouble(), 0.0, value * zMulti.toDouble())
        }
    }
}