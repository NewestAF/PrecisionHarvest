package com.newestaf.precisionHarvest.reward.types

import org.bukkit.entity.Player
import kotlin.random.Random

data class ExpReward(
    val minAmount: Int,
    val maxAmount: Int,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        if (Random.nextDouble(0.0, 100.0) > chance) return

        val amount = if (minAmount > maxAmount) minAmount else (minAmount..maxAmount).random()

        player.giveExp(amount)
    }
}
