package com.newestaf.precisionHarvest.config

import org.bukkit.Material

data class HarvestCategory(
    val id: String,
    val targetBlocks: Set<Material>,
    val allowedToolIds: Set<String>
)
