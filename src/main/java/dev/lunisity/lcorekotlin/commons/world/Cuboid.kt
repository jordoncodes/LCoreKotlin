package dev.lunisity.lcorekotlin.commons.world

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import java.io.Serializable
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class Cuboid() : Serializable {

    companion object {
        lateinit var world: World
        lateinit var worldName: String

        lateinit var minPoint: Location
        lateinit var maxPoint: Location

        var x1: Int? = null
        var x2: Int? = null
        var y1: Int? = null
        var y2: Int? = null
        var z1: Int? = null
        var z2: Int? = null

        lateinit var owner: UUID



        fun createBorder(material: Material?, world: World, minPoint: Location, maxPoint: Location) {
            // Get the minimum and maximum points
            val minX = minPoint.blockX
            val minY = minPoint.blockY
            val minZ = minPoint.blockZ
            val maxX = maxPoint.blockX
            val maxY = maxPoint.blockY
            val maxZ = maxPoint.blockZ

            // Fill bottom border
            for (x in minX..maxX) {
                for (z in minZ..maxZ) {
                    world.getBlockAt(x, minY, z).type = material!!
                }
            }

            // Fill side borders
            for (y in minY + 1 until maxY) {
                world.getBlockAt(minX, y, minZ).type = material!!
                world.getBlockAt(minX, y, maxZ).type = material!!
                world.getBlockAt(maxX, y, minZ).type = material!!
                world.getBlockAt(maxX, y, maxZ).type = material!!
            }
        }

        fun fillCuboid(cuboid: Cuboid, material: Material?) {
            val world: World = Cuboid.world
            val minPoint: Location = Cuboid.minPoint
            val maxPoint: Location = Cuboid.maxPoint

            for (x in minPoint.blockX..maxPoint.blockX) {
                for (y in minPoint.blockY..maxPoint.blockY) {
                    for (z in minPoint.blockZ..maxPoint.blockZ) {
                        val block = world.getBlockAt(x, y, z)
                        block.type = material!!
                    }
                }
            }
        }

    }



    fun Cuboid2(l1: Location, l2: Location) {
        require(l1.world == l2.world) { "Locations must be on the same world" }
        worldName = l1.world!!.name
        x1 = min(l1.blockX.toDouble(), l2.blockX.toDouble()).toInt()
        y1 = min(l1.blockY.toDouble(), l2.blockY.toDouble()).toInt()
        z1 = min(l1.blockZ.toDouble(), l2.blockZ.toDouble()).toInt()
        x2 = max(l1.blockX.toDouble(), l2.blockX.toDouble()).toInt()
        y2 = max(l1.blockY.toDouble(), l2.blockY.toDouble()).toInt()
        z2 = max(l1.blockZ.toDouble(), l2.blockZ.toDouble()).toInt()
    }

    fun Cuboid3(worldName: String?, l1: Location, l2: Location) {
        require(l1.world == l2.world) { "Locations must be on the same world" }
        Cuboid.worldName = l1.world!!.name
        Cuboid.x1 = min(l1.blockX.toDouble(), l2.blockX.toDouble()).toInt()
        Cuboid.y1 = min(l1.blockY.toDouble(), l2.blockY.toDouble()).toInt()
        Cuboid.z1 = min(l1.blockZ.toDouble(), l2.blockZ.toDouble()).toInt()
        Cuboid.x2 = max(l1.blockX.toDouble(), l2.blockX.toDouble()).toInt()
        Cuboid.y2 = max(l1.blockY.toDouble(), l2.blockY.toDouble()).toInt()
        Cuboid.z2 = max(l1.blockZ.toDouble(), l2.blockZ.toDouble()).toInt()
    }

    fun overlaps(other: Cuboid?): Boolean {
        return !((Cuboid.maxPoint.getBlockX() < Cuboid.minPoint.getBlockX()) ||
                (Cuboid.maxPoint.getBlockX() < Cuboid.minPoint.getBlockX()) ||
                (Cuboid.maxPoint.getBlockY() < Cuboid.minPoint.getBlockY()) ||
                (Cuboid.maxPoint.getBlockY() < Cuboid.minPoint.getBlockY()) ||
                (Cuboid.maxPoint.getBlockZ() < Cuboid.minPoint.getBlockZ()) ||
                (Cuboid.maxPoint.getBlockZ() < Cuboid.minPoint.getBlockZ()))
    }

    fun getLocation(): Location {
        return Cuboid.minPoint.clone().add(Cuboid.maxPoint).multiply(0.5)
    }

    fun getBlocks(): Set<Block> {
        val blocks: MutableSet<Block> = HashSet()
        val minX: Double = min(Cuboid.minPoint.getBlockX().toDouble(), Cuboid.maxPoint.getBlockX().toDouble())
        val minY: Double = min(Cuboid.minPoint.getBlockY().toDouble(), Cuboid.maxPoint.getBlockY().toDouble())
        val minZ: Double = min(Cuboid.minPoint.getBlockZ().toDouble(), Cuboid.maxPoint.getBlockZ().toDouble())
        val maxX: Double = max(Cuboid.minPoint.getBlockX().toDouble(), Cuboid.maxPoint.getBlockX().toDouble())
        val maxY: Double = max(Cuboid.minPoint.getBlockY().toDouble(), Cuboid.maxPoint.getBlockY().toDouble())
        val maxZ: Double = max(Cuboid.minPoint.getBlockZ().toDouble(), Cuboid.maxPoint.getBlockZ().toDouble())

        for (x in minX.rangeTo(maxX)) {
            for (y in minY.rangeTo(maxY)) {
                for (z in minZ..maxZ) {
                    blocks.add(world.getBlockAt(x.toInt(), y.toInt(), z.toInt()))
                }
            }
        }

        return blocks
    }

}

private operator fun <T : Comparable<T>> ClosedFloatingPointRange<T>.iterator(): Iterator<T> {
    return object : Iterator<T> {
        var nextValue = start
        override fun hasNext() = nextValue <= endInclusive
        override fun next(): T {
            val current = nextValue
            nextValue = increment(current)
            return current
        }

        private fun increment(current: T): T {
            return when (current) {
                is Double -> current + 1 as T
                is Float -> (current + 1).toFloat() as T
                else -> throw UnsupportedOperationException("Unsupported type")
            }
        }
    }
}

private operator fun <T> Any.plus(t: T): T {
    return t
}

fun iterator(): CuboidIterator {
    return CuboidIterator(Cuboid.world, Cuboid.x1, Cuboid.y1, Cuboid.z1, Cuboid.x2, Cuboid.y2, Cuboid.z2)
}

fun CuboidIterator(w: World, baseX: Int?, baseY: Int?, baseZ: Int?, x2: Int?, y2: Int?, z2: Int?): CuboidIterator {
    TODO("Not yet implemented")
}

class CuboidIterator(
    private val w: World,
    private val baseX: Int,
    private val baseY: Int,
    private val baseZ: Int,
    x2: Int,
    y2: Int,
    z2: Int
) :
    MutableIterator<Block?> {
    private var x: Int
    private var y: Int
    private var z = 0
    private val sizeX = (abs((x2 - baseX).toDouble()) + 1).toInt()
    private val sizeY = (abs((y2 - baseY).toDouble()) + 1).toInt()
    private val sizeZ = (abs((z2 - baseZ).toDouble()) + 1).toInt()

    init {
        this.y = this.z
        this.x = this.y
    }

    override fun hasNext(): Boolean {
        return this.x < this.sizeX && (this.y < this.sizeY) && (this.z < this.sizeZ)
    }

    override fun next(): Block {
        val b = w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z)
        if (++x >= this.sizeX) {
            this.x = 0
            if (++this.y >= this.sizeY) {
                this.y = 0
                ++this.z
            }
        }
        return b
    }

    override fun remove() {
    }

    /*
    fun createRegion(worldName: String?, i: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {
            Cuboid.worldName = worldName!!

            Cuboid.x1 = min(i.toDouble(), x2.toDouble()).toInt()
            Cuboid.y1 = min(y1.toDouble(), y2.toDouble()).toInt()
            Cuboid.z1 = min(z1.toDouble(), z2.toDouble()).toInt()
            Cuboid.x2 = max(i.toDouble(), x2.toDouble()).toInt()
            Cuboid.y2 = max(y1.toDouble(), y2.toDouble()).toInt()
            Cuboid.z2 = max(z1.toDouble(), z2.toDouble()).toInt()
        }
     */
}
