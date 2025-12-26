package com.newestaf.precisionHarvest.reward

import org.bukkit.entity.Player

data class MessageReward(
    val message: String,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        TODO("Not yet implemented")
    }
}
