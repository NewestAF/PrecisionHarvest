package com.newestaf.precisionHarvest

import com.newestaf.precisionHarvest.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class PrecisionHarvest : JavaPlugin() {
    val configManager: ConfigManager = ConfigManager(this)

    override fun onEnable() {
        configManager.loadConfig()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
