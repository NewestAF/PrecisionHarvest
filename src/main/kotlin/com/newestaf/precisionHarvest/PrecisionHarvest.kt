package com.newestaf.precisionHarvest

import com.newestaf.precisionHarvest.config.ConfigManager
import com.newestaf.precisionHarvest.minigame.MinigameManager
import com.newestaf.precisionHarvest.trigger.HarvestTrigger
import org.bukkit.plugin.java.JavaPlugin

class PrecisionHarvest : JavaPlugin() {
    val configManager: ConfigManager = ConfigManager(this)
    val minigameManager: MinigameManager = MinigameManager(this, configManager)

    override fun onEnable() {
        configManager.loadConfig()

        server.pluginManager.registerEvents(HarvestTrigger(configManager, minigameManager), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
