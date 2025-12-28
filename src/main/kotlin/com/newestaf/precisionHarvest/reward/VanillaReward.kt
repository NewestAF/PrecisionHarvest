package com.newestaf.precisionHarvest.reward

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

data class VanillaReward(
    val material: Material,
    val minAmount: Int,
    val maxAmount: Int,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        if (Random.nextDouble(0.0, 100.0) > chance) return

        val item = ItemStack(material)
        item.amount = if (minAmount > maxAmount) minAmount else (minAmount..maxAmount).random()

        player.inventory.addItem(item).values.forEach { remain ->
            player.world.dropItemNaturally(player.location, remain)
        }
        
    }
}