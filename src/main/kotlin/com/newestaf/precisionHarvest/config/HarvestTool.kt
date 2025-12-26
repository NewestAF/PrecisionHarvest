package com.newestaf.precisionHarvest.config

import com.newestaf.precisionHarvest.minigame.GameType
import com.newestaf.precisionHarvest.util.item.ItemMatcher

data class HarvestTool(
    val id: String,
    val matcher: ItemMatcher,
    val gameType: GameType,
    val gameSettings: Map<String, String>,
    val requirements: HarvestRequirements
)
