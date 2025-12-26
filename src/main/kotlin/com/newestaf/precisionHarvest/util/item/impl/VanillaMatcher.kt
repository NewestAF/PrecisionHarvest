package com.newestaf.precisionHarvest.util.item.impl

import com.newestaf.precisionHarvest.util.item.ItemMatcher
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VanillaMatcher(private val material: Material, private val customModelData: Int) : ItemMatcher {

    override fun matches(item: ItemStack?): Boolean {
        if (item == null || item.type == material) return false

        if (customModelData <= 0) return false
        if (!item.hasItemMeta() || !item.itemMeta.hasCustomModelData()) return false
        if (item.itemMeta.customModelData != customModelData) return false

        return true

    }

}