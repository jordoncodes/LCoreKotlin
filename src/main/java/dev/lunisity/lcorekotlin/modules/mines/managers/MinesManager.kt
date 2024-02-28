package dev.lunisity.lcorekotlin.modules.mines.managers

import dev.lunisity.lcorekotlin.LCore.Companion.core
import dev.lunisity.lcorekotlin.commons.world.Cuboid
import dev.lunisity.lcorekotlin.commons.world.Cuboid.Companion.createBorder
import dev.lunisity.lcorekotlin.commons.world.WorldManager.Companion.worldManager
import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import dev.lunisity.lcorekotlin.modules.mines.storage.load.MineLoader.Companion.mineLoader
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver.Companion.mineSaver
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import java.io.File
import kotlin.collections.mutableMapOf
import java.util.*

class MinesManager (manager: MinesManager){

    companion object {

        var mines = mutableMapOf<UUID, PrivateMine>()

        var MINE_SIZE_X: Int = 10
        var MINE_SIZE_Y: Int = 10
        var MINE_SIZE_Z: Int = 10
        var BORDER_MATERIAL: Material = Material.BEDROCK

        lateinit var manager: MinesManager

    }



    fun enable() {
        manager = this
        loadAll()
    }

    private fun getMine(uuid: UUID): PrivateMine? {
        return mines[uuid]
    }

    fun createMine(owner: Player) {

        val location: Location = worldManager.nextFreeLocation()

        val blockX: Int = location.blockX
        val blockY: Int = location.blockY
        val blockZ: Int = location.blockZ

        val minX: Int = location.blockX - MINE_SIZE_X / 2 - 1
        val minY: Int = location.blockY
        val minZ: Int = location.blockZ - MINE_SIZE_Z / 2 - 1
        val maxX: Int = location.blockX + MINE_SIZE_X / 2 + 1
        val maxY: Int = location.blockY + MINE_SIZE_Y
        val maxZ: Int = location.blockZ + MINE_SIZE_Z / 2 + 1

        val min: Location = Location(location.world, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val max: Location = Location(location.world, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        val region: Cuboid = createRegion(worldManager.getMinesWorld(), min, max, owner.uniqueId)
        createBorder(BORDER_MATERIAL, worldManager.getMinesWorld(), min, max)
        fillMine(region, startingBlock(), location)

        val loc: Location = Location(location.world, blockX.toDouble(), blockY.toDouble() + 3, blockZ.toDouble())

        val newMine: PrivateMine = PrivateMine(owner.uniqueId, region, loc)
        mines.put(owner.uniqueId, newMine)

        newMine.teleport = loc
        owner.teleport(loc)

        mineSaver.save(newMine, owner.uniqueId)
    }

    private fun createRegion(world: World, loc1: Location, loc2: Location, owner: UUID): Cuboid {

        val mineWorld: World = worldManager.getMinesWorld()

        val minX: Int = loc1.blockX - MINE_SIZE_X / 2 - 1
        val minY: Int = loc1.blockY
        val minZ: Int = loc1.blockZ - MINE_SIZE_Z / 2 - 1
        val maxX: Int = loc2.blockX + MINE_SIZE_X / 2 + 1
        val maxY: Int = loc2.blockY + MINE_SIZE_Y
        val maxZ: Int = loc2.blockZ + MINE_SIZE_Z / 2 + 1

        val min: Location = Location(mineWorld, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val max: Location = Location(mineWorld, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        return createRegion(mineWorld, min, max, owner)
    }

    fun getMines(): Collection<PrivateMine> {
        return mines.values
    }

    private fun fillMine(cuboid: Cuboid, material: Material, location: Location) {
        val world: World = worldManager.getMinesWorld()

        var blockX: Int = location.blockX
        var blockY: Int = location.blockY
        var blockZ: Int = location.blockZ

        val minX: Int = location.blockX - MINE_SIZE_X / 2 - 1
        val minY: Int = location.blockY
        val minZ: Int = location.blockZ - MINE_SIZE_Z / 2 - 1
        val maxX: Int = location.blockX + MINE_SIZE_X / 2 + 1
        val maxY: Int = location.blockY + MINE_SIZE_Y
        val maxZ: Int = location.blockZ + MINE_SIZE_Z / 2 + 1

        val minPoint: Location = Location(world, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val maxPoint: Location = Location(world, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        for (x in minPoint.blockX..maxPoint.blockX) {
            for (y in minPoint.blockY..maxPoint.blockY) {
                for (z in minPoint.blockZ..maxPoint.blockZ) {
                    var block = world.getBlockAt(x, y, z)
                    block.type = material
                }
            }
        }
    }

    fun teleportMine(player: Player) {
        val mine: PrivateMine = getMine(player.uniqueId) ?: return

        if (mine == null) {
            return
        }

        player.teleport(mine.teleport)

    }

    fun adminTeleportMine(player: Player, target: Player) {
        val mine: PrivateMine = getMine(target.uniqueId) ?: return

        if (mine == null) {
            return
        }

        player.teleport(mine.teleport)

    }

    fun startingBlock(): Material {
        return Material.STONE
    }

    private fun loadAll() {
        val directory: File = File(core.dataFolder.path, "/mines/")
        val allFiles: Array<File> = directory.listFiles() ?: arrayOf()

        mines = mutableMapOf<UUID, PrivateMine>()

        if (allFiles.isEmpty()) {
            return
        }

        for (file: File in allFiles) {

            if (!file.name.endsWith(".json")) {
                continue
            }

            val mine: PrivateMine? = mineLoader.load(file)

            val uuid: UUID = mine?.owner ?: continue

            if (mine == null) {
                continue
            }

            mines.put(uuid, mine)

            core.logger.info("Loaded mine for $uuid")

        }

    }

    fun disable() {
        mineSaver.saveAll()
    }
}