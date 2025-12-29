package com.newestaf.precisionHarvest.minigame

import com.newestaf.precisionHarvest.config.HarvestCategory
import com.newestaf.precisionHarvest.config.HarvestTool
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class MinigameManager(
    private val plugin: JavaPlugin,
) : Listener {

    private val activeGames: HashMap<UUID, AbstractMinigame> = HashMap()

    fun start(player: Player,
              tool: HarvestTool,
              category: HarvestCategory,
              block: Block) {
        if (activeGames.containsKey(player.uniqueId)) return

        val game = MinigameFactory.getMinigame(plugin, player, tool) ?: return
        activeGames[player.uniqueId] = game

        game.start { result ->
            handleGameResult(player, result, category, block)
            activeGames.remove(player.uniqueId)
        }
    }

    fun shutdown() {
        activeGames.values.forEach { it.end(GameResult.FAILURE) }
        activeGames.clear()
    }


    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId
        activeGames[uuid]?.let { game ->
            game.end(GameResult.FAILURE)
            activeGames.remove(uuid)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        if (holder is AbstractMinigame) {
            event.isCancelled = true
            holder.onClick(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        if (holder is AbstractMinigame) {
            holder.onForcedClose(event)
        }
    }

    private fun handleGameResult(player: Player,
                         result: GameResult,
                         category: HarvestCategory,
                         block: Block) {
        when (result) {
            GameResult.SUCCESS -> {
                category.successReward.forEach { it.give(player) }
                if (category.doBreakOnSuccess) {
                    block.type = Material.AIR
                    player.playSound(block.location, Sound.BLOCK_STONE_BREAK, 1f, 1f)
                    player.world.spawnParticle(Particle.BLOCK,
                        block.location.add(0.5, 0.5, 0.5),
                        20, 0.3, 0.3, 0.3, block.blockData)
                }
            }
            GameResult.FAILURE -> {
                category.failureReward.forEach { it.give(player) }
                if (category.doBreakOnFailure) {
                    block.type = Material.AIR
                    player.playSound(block.location, Sound.BLOCK_STONE_BREAK, 1f, 1f)
                    player.world.spawnParticle(Particle.BLOCK,
                        block.location.add(0.5, 0.5, 0.5),
                        20, 0.3, 0.3, 0.3, block.blockData)
                }
            }
        }

    }
}