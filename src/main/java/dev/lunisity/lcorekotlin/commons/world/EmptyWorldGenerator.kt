package dev.lunisity.lcorekotlin.commons.world

import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import org.jetbrains.annotations.NotNull
import java.util.*

class EmptyWorldGenerator : ChunkGenerator() {

    @Deprecated("Deprecated in Java", ReplaceWith("createChunkData(world)"))
    @NotNull
    @Suppress("deprecation")
    override fun generateChunkData(world: World, random: Random, cx: Int, cz: Int, biome: ChunkGenerator.BiomeGrid): ChunkGenerator.ChunkData {
        return createChunkData(world)
    }
}