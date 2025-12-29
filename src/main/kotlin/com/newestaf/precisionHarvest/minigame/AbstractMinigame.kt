package com.newestaf.precisionHarvest.minigame

import com.newestaf.precisionHarvest.config.HarvestTool
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class AbstractMinigame(
    protected val player: Player,
    protected val tool: HarvestTool
): InventoryHolder {

    protected val mm: MiniMessage = MiniMessage.miniMessage()
    protected var onFinish: ((GameResult) -> Unit)? = null
    protected var inventory: Inventory? = null
    protected abstract val soundEffect: Sound?
    var isRunning: Boolean = false
        protected set

    fun start(callback: (GameResult) -> Unit) {
        onFinish = callback
        isRunning = true

        inventory = createInventory()
        initialize()

        inventory?.let { player.openInventory(it) }
        soundEffect?.let { player.playSound(player.location, it, 1f, 1f) }
    }

    fun end(result: GameResult) {
        if (!isRunning) return

        isRunning = false
        cleanUp()
        onFinish?.invoke(result)

        if (player.openInventory.topInventory == inventory) player.closeInventory()
    }

    protected abstract fun createInventory(): Inventory
    protected abstract fun initialize()
    protected abstract fun cleanUp()

    abstract fun onClick(event: InventoryClickEvent)

    override fun getInventory(): Inventory {
        return inventory
            ?: Bukkit.createInventory(this, 9, Component.text("Initializing..."))
    }

    fun onForcedClose(event: InventoryCloseEvent) {
        if (isRunning) {
            end(GameResult.FAILURE)
        }
    }

    protected fun getIntSetting(key: String, default: Int): Int {
        return tool.gameSettings[key]?.toIntOrNull() ?: default
    }

    protected fun getDoubleSetting(key: String, default: Double): Double {
        return tool.gameSettings[key]?.toDoubleOrNull() ?: default
    }

    protected fun getStringSetting(key: String, default: String): String {
        return tool.gameSettings[key] ?: default
    }

}