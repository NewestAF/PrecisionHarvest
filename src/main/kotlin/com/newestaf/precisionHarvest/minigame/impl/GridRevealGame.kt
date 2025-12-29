package com.newestaf.precisionHarvest.minigame.impl

import com.newestaf.precisionHarvest.config.HarvestTool
import com.newestaf.precisionHarvest.minigame.AbstractMinigame
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin

class GridRevealGame(
    plugin: JavaPlugin,
    player: Player,
    tool: HarvestTool
) : AbstractMinigame(plugin, player, tool) {
    override val soundEffect: Sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP

    override fun createInventory(): Inventory {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        TODO("Not yet implemented")
    }

    override fun cleanUp() {
        TODO("Not yet implemented")
    }

    override fun onClick(event: InventoryClickEvent) {
        TODO("Not yet implemented")
    }
}