package dev.lunisity.lcorekotlin.modules.mines.model

import dev.lunisity.lcorekotlin.commons.world.Cuboid
import org.bukkit.Location
import java.util.UUID

class PrivateMine(
    var owner: UUID,
    var region: Cuboid,
    var teleport: Location
) {


}