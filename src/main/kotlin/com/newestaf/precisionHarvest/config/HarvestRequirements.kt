package com.newestaf.precisionHarvest.config

data class HarvestRequirements(
    val permission: String,
    val staminaCost: Double = 0.0,
    val manaCost: Double = 0.0,
    val durabilityCost: Int = 0
)
