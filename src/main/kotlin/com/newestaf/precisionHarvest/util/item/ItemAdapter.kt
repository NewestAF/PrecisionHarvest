package com.newestaf.precisionHarvest.util.item

import com.newestaf.precisionHarvest.util.item.impl.MMOItemsAdapter
import com.newestaf.precisionHarvest.util.item.impl.VanillaAdapter
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface ItemAdapter {
    fun matches(item: ItemStack?): Boolean
    fun checkDurability(item: ItemStack, cost: Int): Boolean = true
    fun damageItem(player: Player, item: ItemStack, cost: Int) {}

    companion object {
        fun from(section: ConfigurationSection): ItemAdapter? {
            val typeStr = section.getString("type", "VANILLA")!!.uppercase()
            val value = section.getString("value", "")!!

            return when (typeStr) {
                "MMOITEMS" -> MMOItemsAdapter(value)
                "VANILLA" -> {
                    val material =
                        Material.matchMaterial(section.getString("material", "AIR")!!)
                            ?: Material.AIR
                    val cmd = section.getInt("custom_model_data", 0)
                    VanillaAdapter(material, cmd)
                }

                else -> null
            }
        }
    }
}