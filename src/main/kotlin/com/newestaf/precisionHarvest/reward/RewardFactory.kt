package com.newestaf.precisionHarvest.reward

import com.newestaf.precisionHarvest.reward.types.DamageReward
import com.newestaf.precisionHarvest.reward.types.ExpReward
import com.newestaf.precisionHarvest.reward.types.HarvestReward
import com.newestaf.precisionHarvest.reward.types.MMOItemReward
import com.newestaf.precisionHarvest.reward.types.MessageReward
import com.newestaf.precisionHarvest.reward.types.VanillaReward
import org.bukkit.Material

object RewardFactory {
    fun parseList(list: List<Map<*, *>>) : List<HarvestReward> {
        @Suppress("unchecked_cast")
        return list.mapNotNull { parse(it as Map<String, Any>) }
    }

    private fun parse(map: Map<String, Any>): HarvestReward? {
        val typeStr = (map["type"] as? String)?.uppercase() ?: return null
        val chance = (map["chance"] as? Number)?.toDouble() ?: return null

        return try {
            when (RewardType.valueOf(typeStr)) {
                RewardType.VANILLA -> {
                    val material = Material.matchMaterial(map["material"] as String) ?: return null
                    val (min, max) = parseAmount(map["amount"])
                    VanillaReward(material, min.toInt(), max.toInt(), chance)
                }
                RewardType.MMOITEMS -> {
                    val fullId = map["id"] as String
                    val split = fullId.split(":")
                    val (min, max) = parseAmount(map["amount"])
                    MMOItemReward(split[0], split[1], min.toInt(), max.toInt(), chance)
                }
                RewardType.EXP -> {
                    val (min, max) = parseAmount(map["amount"])
                    ExpReward(min.toInt(), max.toInt(), chance)
                }
                RewardType.DAMAGE -> {
                    val (min, max) = parseAmount(map["amount"])
                    DamageReward(min.toDouble(), max.toDouble(), chance)
                }
                RewardType.MESSAGE -> {
                    MessageReward(map["text"] as? String ?: "", chance)
                }

            }
        } catch (e: Exception) {
            null
        }

    }

    private fun parseAmount(obj: Any?): Pair<Number, Number> {
        return when (obj) {
            is Number -> obj to obj
            is String -> {
                if (obj.contains("-")) {
                    val split = obj.split("-")
                    Pair(split[0].trim().toDoubleOrNull() ?: 0.0, split[1].trim().toDoubleOrNull() ?: 0.0)
                } else {
                    val v = obj.trim().toDoubleOrNull() ?: 0.0
                    v to v
                }
            }
            else -> 1 to 1
        }

    }
}