package com.newestaf.precisionHarvest.reward

import org.bukkit.entity.Player
import kotlin.random.Random

data class DamageReward(
    val minAmount: Double,
    val maxAmount: Double,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        if (Random.nextDouble(0.0, 100.0) > chance) return

        val amount = if (minAmount > maxAmount) minAmount else Random.nextDouble(minAmount, maxAmount)
        player.damage(amount)
    }
}
