package com.newestaf.precisionHarvest.trigger

import com.newestaf.precisionHarvest.config.HarvestTool
import net.Indyuce.mmocore.api.event.PlayerResourceUpdateEvent
import net.Indyuce.mmocore.api.player.PlayerData
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.GameMode
import org.bukkit.entity.Player

class RequirementChecker {

    private val mm = MiniMessage.miniMessage()

    fun check(player: Player, tool: HarvestTool): Boolean {
        val req = tool.requirements
        req.permission?.let { perm ->
            if (!player.hasPermission(perm)) {
                player.sendMessage(mm.deserialize("<red>이 도구를 사용할 권한이 없습니다."))
                return false
            }
        }
        if (player.gameMode == GameMode.CREATIVE) return true

        val item = player.inventory.itemInMainHand

        if (!tool.adapter.checkDurability(item, req.durabilityCost)) {
            player.sendMessage(mm.deserialize("<red>도구의 내구도가 부족하여 사용할 수 없습니다."))
            return false
        }

        if (req.staminaCost > 0 || req.manaCost > 0) {
            val playerData = PlayerData.get(player.uniqueId)

            if (playerData.stamina < req.staminaCost) {
                player.sendMessage(mm.deserialize("<red>스태미나가 부족합니다."))
                return false
            }
            if (playerData.mana < req.manaCost) {
                player.sendMessage(mm.deserialize("<red>마나가 부족합니다."))
                return false
            }
        }

        return true
    }

    fun consume(player: Player, tool: HarvestTool) {
        if (player.gameMode == GameMode.CREATIVE) return

        val req = tool.requirements

        if (req.staminaCost > 0 || req.manaCost > 0) {
            val playerData = PlayerData.get(player.uniqueId)
            val reason = PlayerResourceUpdateEvent.UpdateReason.SKILL_COST

            if (req.staminaCost > 0) playerData.giveStamina(-req.staminaCost, reason)
            if (req.manaCost > 0) playerData.giveMana(-req.manaCost, reason)
        }

        if (req.durabilityCost > 0) {
            val item = player.inventory.itemInMainHand

            tool.adapter.damageItem(player, item, req.durabilityCost)
        }
    }
}