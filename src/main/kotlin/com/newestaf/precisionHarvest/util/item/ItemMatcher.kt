package com.newestaf.precisionHarvest.util.item

import org.bukkit.inventory.ItemStack

interface ItemMatcher {
    fun matches(item: ItemStack?): Boolean
}