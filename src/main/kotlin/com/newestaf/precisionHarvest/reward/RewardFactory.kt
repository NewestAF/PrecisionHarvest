package com.newestaf.precisionHarvest.reward

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
                    VanillaReward(material, min, max, chance)
                }
                RewardType.MMOITEMS -> {
                    val fullId = map["id"] as String
                    val split = fullId.split(":")
                    val (min, max) = parseAmount(map["amount"])
                    MMOItemReward(split[0], split[1], min, max, chance)
                }
                RewardType.EXP -> {
                    val (min, max) = parseAmount(map["amount"])
                    ExpReward(min, max, chance)
                }
                RewardType.DAMAGE -> {
                    val (min, max) = parseAmount(map["amount"])
                    DamageReward(min, max, chance)
                }
                RewardType.MESSAGE -> {
                    MessageReward(map["message"] as? String ?: "", chance)
                }

            }
        } catch (e: Exception) {
            null
        }

    }

    private fun parseAmount(obj: Any?): Pair<Int, Int> {
        return when (obj) {
            is Number -> obj.toInt() to obj.toInt()
            is String -> {
                if (obj.contains("-")) {
                    val split = obj.split("-")
                    Pair(split[0].toInt(), split[1].toInt())
                } else {
                    val v = obj.toInt()
                    v to v
                }
            }
            else -> 1 to 1
        }

    }
}