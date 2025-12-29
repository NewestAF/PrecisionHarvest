package com.newestaf.precisionHarvest.reward.types

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import kotlin.random.Random

data class MessageReward(
    val message: String,
    override val chance: Double
) : HarvestReward {
    override fun give(player: Player) {
        if (Random.nextDouble(0.0, 100.0) > chance) return

        player.sendMessage(MiniMessage.miniMessage().deserialize(message))
    }
}
