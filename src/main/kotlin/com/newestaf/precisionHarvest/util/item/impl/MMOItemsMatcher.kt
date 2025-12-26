package com.newestaf.precisionHarvest.util.item.impl

import com.newestaf.precisionHarvest.util.item.ItemMatcher
import io.lumine.mythic.lib.api.item.NBTItem
import org.bukkit.inventory.ItemStack

class MMOItemsMatcher(matchValue: String) : ItemMatcher {
    private val targetType: String
    private val targetId: String

    init {
        val parts = matchValue.split(":")

        if (parts.size == 2) {
            targetType = parts[0]
            targetId = parts[1]

        }
        else {
            targetType = "INVAILD"
            targetId = "INVAILD"
        }
    }

    override fun matches(item: ItemStack?): Boolean {
        if (item == null ||  item.type.isAir) return false

        val nbtItem = NBTItem.get(item)

        if (!nbtItem.hasTag("MMOITEMS_ITEM_ID")) return false

        return nbtItem.getString("MMOITEMS_ITEM_TYPE").equals(targetType.uppercase()) &&
                nbtItem.getString("MMOITEMS_ITEM_ID").equals(targetId.uppercase())
    }
}