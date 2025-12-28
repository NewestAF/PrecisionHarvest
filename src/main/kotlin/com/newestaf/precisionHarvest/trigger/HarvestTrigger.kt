package com.newestaf.precisionHarvest.trigger

import com.newestaf.precisionHarvest.config.ConfigManager
import com.newestaf.precisionHarvest.minigame.MinigameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class HarvestTrigger(
    private val configManager: ConfigManager,
    private val minigameManager: MinigameManager
) : Listener {

    private val checker: RequirementChecker = RequirementChecker()

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onHarvest(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.hand != EquipmentSlot.HAND) return

        val block = event.clickedBlock ?: return
        val player = event.player
        val item = player.inventory.itemInMainHand

        val (category, tool) = configManager.findCategory(block.type, item) ?: return

        if (!checker.check(player, tool)) return

        event.isCancelled = true
        checker.consume(player, tool)

        minigameManager.start(player, tool, category, block)
    }
}