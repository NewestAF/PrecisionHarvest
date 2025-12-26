package com.newestaf.precisionHarvest.config

import org.bukkit.Material
import org.bukkit.plugin.Plugin

class ConfigManager(private val plugin: Plugin) {

    private val toolMap = HashMap<String, HarvestTool>()
    private val categoryMap = HashMap<String, HarvestCategory>()
    private val blockLockupCache = HashMap<Material, HarvestCategory>()

    fun loadConfig() {

    }

    private fun loadTool() {

    }

    private fun loadCategories() {

    }

}