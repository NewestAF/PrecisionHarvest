package com.newestaf.precisionHarvest.config

import com.newestaf.precisionHarvest.reward.types.HarvestReward
import org.bukkit.Material

data class HarvestCategory(
    val id: String,
    val targetBlocks: Set<Material>,
    val allowedToolIds: Set<String>,
    val doBreakOnSuccess: Boolean,
    val doBreakOnFailure: Boolean,
    val successReward: List<HarvestReward>,
    val failureReward: List<HarvestReward>
)
