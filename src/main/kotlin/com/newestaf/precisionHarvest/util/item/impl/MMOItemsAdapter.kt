package com.newestaf.precisionHarvest.util.item.impl

import com.newestaf.precisionHarvest.util.VanillaDurabilityUtil
import com.newestaf.precisionHarvest.util.item.ItemAdapter
import io.lumine.mythic.lib.api.item.NBTItem
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class MMOItemsAdapter(matchValue: String) : ItemAdapter {
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

    override fun checkDurability(item: ItemStack, cost: Int): Boolean {
        val nbtItem = NBTItem.get(item)
        if (!nbtItem.hasType()) return false

        if (nbtItem.hasTag("MMOITEMS_DURABILITY")) {
            val current = nbtItem.getInteger("MMOITEMS_DURABILITY")
            return current >= cost
        }

        return VanillaDurabilityUtil.checkHasEnough(item, cost)
    }

    override fun damageItem(player: Player, item: ItemStack, cost: Int) {
        val nbtItem = NBTItem.get(item)
        if (!nbtItem.hasTag("MMOITEMS_DURABILITY") || !nbtItem.hasTag("MMOITEMS_MAX_DURABILITY")) {
            VanillaDurabilityUtil.damageItem(player, item, cost)
            return
        }

        val maxDurability = nbtItem.getInteger("MMOITEMS_MAX_DURABILITY")
        val current = nbtItem.getInteger("MMOITEMS_DURABILITY")
        val newDurability = current - cost

        if (newDurability <= 0) {
            player.inventory.setItemInMainHand(null)
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            return
        }

        nbtItem.setInteger("MMOITEMS_DURABILITY", newDurability)

        val itemTypeMaxDurability = item.type.maxDurability

        val damageRatio = 1.0 - newDurability.toDouble() / maxDurability.toDouble()
        val vanillaDamage = (itemTypeMaxDurability * damageRatio).toInt()

        val newItem = nbtItem.toItem()

        val meta = newItem.itemMeta as? Damageable ?: return
        meta.damage = vanillaDamage
        newItem.itemMeta = meta

        player.inventory.setItemInMainHand(newItem)
    }
}