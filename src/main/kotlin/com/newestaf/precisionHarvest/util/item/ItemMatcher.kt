package com.newestaf.precisionHarvest.util.item

import com.newestaf.precisionHarvest.util.item.impl.MMOItemsMatcher
import com.newestaf.precisionHarvest.util.item.impl.VanillaMatcher
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

interface ItemMatcher {
    fun matches(item: ItemStack?): Boolean

    companion object {
        fun from(section: ConfigurationSection): ItemMatcher {
            val typeStr = section.getString("type", "VANILLA")!!.uppercase()
            val value = section.getString("value", "")!!

            return when (typeStr) {
                "MMOITEMS" -> MMOItemsMatcher(value)
                "VANILLA" -> {
                    val material =
                        Material.matchMaterial(section.getString("material", "AIR")!!)
                            ?: Material.AIR
                    val cmd = section.getInt("custom_model_data", 0)
                    VanillaMatcher(material, cmd)
                }

                else -> VanillaMatcher(Material.AIR, 0)
            }

        }
    }
}