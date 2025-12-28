package com.newestaf.precisionHarvest.util.item.impl

import com.newestaf.precisionHarvest.util.VanillaDurabilityUtil
import com.newestaf.precisionHarvest.util.item.ItemAdapter
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class VanillaAdapter(private val material: Material, private val customModelData: Int) : ItemAdapter {

    override fun matches(item: ItemStack?): Boolean {
        if (item == null || item.type == material) return false

        if (customModelData <= 0) return true
        if (!item.hasItemMeta() || !item.itemMeta.hasCustomModelData()) return false
        if (item.itemMeta.customModelData != customModelData) return false

        return true

    }

    override fun checkDurability(item: ItemStack, cost: Int): Boolean {
        return VanillaDurabilityUtil.checkHasEnough(item, cost)
    }

    override fun damageItem(player: Player, item: ItemStack, cost: Int) {
        VanillaDurabilityUtil.damageItem(player, item, cost)
    }

}