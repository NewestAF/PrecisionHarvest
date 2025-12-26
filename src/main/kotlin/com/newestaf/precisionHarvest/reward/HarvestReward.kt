package com.newestaf.precisionHarvest.reward

import org.bukkit.entity.Player

sealed interface HarvestReward {
    val chance: Double

    fun give(player: Player)
}