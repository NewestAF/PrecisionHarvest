package com.newestaf.precisionHarvest.minigame

import com.newestaf.precisionHarvest.config.HarvestTool
import com.newestaf.precisionHarvest.minigame.impl.GridRevealGame
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object MinigameFactory {

    fun getMinigame(plugin: JavaPlugin, player: Player, tool: HarvestTool) : AbstractMinigame? {
        return when (tool.gameType) {
            GameType.GRID_REVEAL -> GridRevealGame(plugin, player, tool)

            else -> null
        }
    }

}