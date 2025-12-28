package com.newestaf.precisionHarvest.reward

import net.Indyuce.mmoitems.MMOItems
import org.bukkit.entity.Player
import kotlin.random.Random

data class MMOItemReward(
    val type: String,
    val id: String,
    val minAmount: Int,
    val maxAmount: Int,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        if (Random.nextDouble(0.0, 100.0) > chance) return

        val item = MMOItems.plugin.getItem(type, id) ?: return
        item.amount = if (minAmount > maxAmount) minAmount else (minAmount..maxAmount).random()

        player.inventory.addItem(item).values.forEach { remain ->
            player.world.dropItemNaturally(player.location, remain)
        }
    }
}
