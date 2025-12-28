package com.newestaf.precisionHarvest.util

import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.random.Random

object VanillaDurabilityUtil {
    fun checkHasEnough(item: ItemStack, cost: Int): Boolean {
        if (!item.hasItemMeta() || item.itemMeta !is Damageable) return true
        val meta = item.itemMeta as Damageable
        val maxDurability = item.type.maxDurability

        return maxDurability <= 0 || (maxDurability - meta.damage) > cost
    }

    fun damageItem(player: Player, item: ItemStack, cost: Int) {
        if (item.type.isAir) return
        val meta = item.itemMeta as? Damageable ?: return

        val unbreakableLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING)
        var damageToApply = 0

        repeat (cost) {
            if (shouldTakeDamage(unbreakableLevel)) {
                damageToApply++
            }
        }

        if (damageToApply > 0) {
            val newDamage = meta.damage + damageToApply

            if (newDamage >= item.type.maxDurability) {
                player.inventory.setItemInMainHand(null)
                player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            }
            else {
                meta.damage = newDamage
                item.itemMeta = meta
                player.inventory.setItemInMainHand(item)
            }
        }
    }

    private fun shouldTakeDamage(level: Int): Boolean {
        if (level <= 0) return true
        return Random.nextDouble() < (1.0 / (level + 1))

    }
}