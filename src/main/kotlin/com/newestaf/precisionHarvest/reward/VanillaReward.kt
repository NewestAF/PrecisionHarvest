package com.newestaf.precisionHarvest.reward

import org.bukkit.Material
import org.bukkit.entity.Player

data class VanillaReward(
    val material: Material,
    val minAmount: Int,
    val maxAmount: Int,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        TODO("Not yet implemented")
    }
}