package dev.lunisity.lcorekotlin.modules.mines.managers

import dev.lunisity.lcorekotlin.LCore
import dev.lunisity.lcorekotlin.commons.world.Cuboid
import dev.lunisity.lcorekotlin.commons.world.Cuboid.Companion.createBorder
import dev.lunisity.lcorekotlin.commons.world.WorldManager.Companion.worldManager
import dev.lunisity.lcorekotlin.modules.mines.model.PrivateMine
import dev.lunisity.lcorekotlin.modules.mines.storage.load.MineLoader
import dev.lunisity.lcorekotlin.modules.mines.storage.save.MineSaver
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import java.io.File
import kotlin.collections.mutableMapOf
import java.util.*

class MinesManager(
    private val core: LCore,
    ) {


    val mineSaver = MineSaver(core, this)
    val mineLoader = MineLoader(core)

    companion object {

        var mines = mutableMapOf<UUID, PrivateMine>()

        var MINE_SIZE_X: Int = 10
        var MINE_SIZE_Y: Int = 10
        var MINE_SIZE_Z: Int = 10
        var BORDER_MATERIAL: Material = Material.BEDROCK

    }

    fun enable() {
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

        val min = Location(location.world, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val max = Location(location.world, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        val region: Cuboid = createRegion(min, max, owner.uniqueId)
        createBorder(BORDER_MATERIAL, worldManager.getMinesWorld(), min, max)
        fillMine(startingBlock(), location)

        val loc = Location(location.world, blockX.toDouble(), blockY.toDouble() + 3, blockZ.toDouble())

        val newMine = PrivateMine(owner.uniqueId, region, loc)
        mines.put(owner.uniqueId, newMine)

        newMine.teleport = loc
        owner.teleport(loc)

        mineSaver.save(newMine, owner.uniqueId)
    }

    private fun createRegion(loc1: Location, loc2: Location, owner: UUID): Cuboid {

        val mineWorld: World = worldManager.getMinesWorld()

        val minX: Int = loc1.blockX - MINE_SIZE_X / 2 - 1
        val minY: Int = loc1.blockY
        val minZ: Int = loc1.blockZ - MINE_SIZE_Z / 2 - 1
        val maxX: Int = loc2.blockX + MINE_SIZE_X / 2 + 1
        val maxY: Int = loc2.blockY + MINE_SIZE_Y
        val maxZ: Int = loc2.blockZ + MINE_SIZE_Z / 2 + 1

        val min = Location(mineWorld, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val max = Location(mineWorld, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        return createRegion(min, max, owner)
    }

    fun getMines(): Collection<PrivateMine> {
        return mines.values
    }

    private fun fillMine(material: Material, location: Location) {
        val world: World = worldManager.getMinesWorld()

        val minX: Int = location.blockX - MINE_SIZE_X / 2 - 1
        val minY: Int = location.blockY
        val minZ: Int = location.blockZ - MINE_SIZE_Z / 2 - 1
        val maxX: Int = location.blockX + MINE_SIZE_X / 2 + 1
        val maxY: Int = location.blockY + MINE_SIZE_Y
        val maxZ: Int = location.blockZ + MINE_SIZE_Z / 2 + 1

        val minPoint = Location(world, minX.toDouble(), minY.toDouble(), minZ.toDouble())
        val maxPoint = Location(world, maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

        for (x in minPoint.blockX..maxPoint.blockX) {
            for (y in minPoint.blockY..maxPoint.blockY) {
                for (z in minPoint.blockZ..maxPoint.blockZ) {
                    val block = world.getBlockAt(x, y, z)
                    block.type = material
                }
            }
        }
    }

    fun teleportMine(player: Player) {
        val mine: PrivateMine = getMine(player.uniqueId) ?: return

        player.teleport(mine.teleport)

    }

    fun adminTeleportMine(player: Player, target: Player) {
        val mine: PrivateMine = getMine(target.uniqueId) ?: return

        player.teleport(mine.teleport)

    }

    private fun startingBlock(): Material {
        return Material.STONE
    }

    private fun loadAll() {
        val directory = File(core.dataFolder.path, "/mines/")
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

            mines.put(uuid, mine)

            core.logger.info("Loaded mine for $uuid")

        }

    }

    fun disable() {
        mineSaver.saveAll()
    }
}