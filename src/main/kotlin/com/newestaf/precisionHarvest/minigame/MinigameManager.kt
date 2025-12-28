package com.newestaf.precisionHarvest.minigame

import com.newestaf.precisionHarvest.config.ConfigManager
import com.newestaf.precisionHarvest.config.HarvestCategory
import com.newestaf.precisionHarvest.config.HarvestTool
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

class MinigameManager(
    private val plugin: Plugin,
    private val configManager: ConfigManager
) {

    private val activeGames: HashMap<UUID, AbstractMinigame> = HashMap()

    fun start(player: Player, tool: HarvestTool, category: HarvestCategory, block: Block) {
        // TODO
    }
}