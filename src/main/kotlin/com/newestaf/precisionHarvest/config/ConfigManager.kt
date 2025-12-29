package com.newestaf.precisionHarvest.config

import com.newestaf.precisionHarvest.minigame.GameType
import com.newestaf.precisionHarvest.reward.RewardFactory
import com.newestaf.precisionHarvest.util.item.ItemAdapter
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.EnumMap

class ConfigManager(private val plugin: JavaPlugin) {

    private val toolMap = HashMap<String, HarvestTool>()
    private val categoryMap = HashMap<String, HarvestCategory>()
    private val blockLookupCache = EnumMap<Material, MutableList<HarvestCategory>>(Material::class.java)

    fun loadConfig() {
        toolMap.clear()
        loadTools()

        categoryMap.clear()
        blockLookupCache.clear()
        loadCategories()

        plugin.logger.info("PrecisionHarvest 로드 완료: 도구 ${toolMap.size}개, 카테고리 ${categoryMap.size}개")
    }

    fun findCategory(material: Material, toolItem: ItemStack): Pair<HarvestCategory, HarvestTool>? {
        val potentialCategories = blockLookupCache[material] ?: return null

        return potentialCategories.firstNotNullOfOrNull { category ->
            category.allowedToolIds
                .mapNotNull { toolMap[it] }
                .firstOrNull { it.adapter.matches(toolItem) }
                ?.let { tool -> category to tool }
        }

    }

    fun getTool(id: String): HarvestTool? = toolMap[id]

    private fun loadTools() {
        val toolConfig = loadYaml("tools.yml")

        val toolRoot = toolConfig.getConfigurationSection("tools") ?: run {
            plugin.logger.warning("도구 로드 실패: tools.yml을 점검하시길 바랍니다.")
            return
        }

        for (toolId in toolRoot.getKeys(false)) {
            val section = toolRoot.getConfigurationSection(toolId) ?: continue
            val tool = loadTool(toolId, section) ?: continue
            toolMap[toolId] = tool
        }
    }

    private fun loadCategories() {
        val categoryConfig = loadYaml("category.yml")

        val categoryRoot = categoryConfig.getConfigurationSection("categories") ?: run {
            plugin.logger.warning("카테고리 로드 실패: category.yml을 점검하시길 바랍니다.")
            return
        }

        for (categoryId in categoryRoot.getKeys(false)) {
            val section = categoryRoot.getConfigurationSection(categoryId) ?: continue
            val category = loadCategory(categoryId, section) ?: continue
            categoryMap[categoryId] = category
            category.targetBlocks.forEach { targetBlock ->
                val list = blockLookupCache.getOrPut(targetBlock) { ArrayList() }
                list.add(category)
            }
        }
    }

    private fun loadTool(id: String, section: ConfigurationSection): HarvestTool? {
        val matcherSection = section.getConfigurationSection("matcher") ?: run {
            plugin.logger.warning("도구 '$id' 로드 실패: 'matcher' 섹션이 누락되었습니다.")
            return null
        }

        val matcher = try {
            ItemAdapter.from(matcherSection) ?: run {
                plugin.logger.warning("도구 '$id' 로드 실패: 유효하지 않은 Matcher 입니다.")
                return null
            }
        } catch (e: Exception) {
            plugin.logger.warning("도구 '$id' 로드 실패: Matcher 생성 중 오류 발생 - ${e.message}")
            return null
        }

        val gameTypeStr = section.getString("game_type")
        val gameType = runCatching {
            GameType.valueOf(gameTypeStr?.uppercase() ?: "")
        }.getOrElse {
            plugin.logger.warning("도구 '$id' 로드 실패: 유효하지 않은 game_type입니다. - ${gameTypeStr}")
            return null
        }

        val gameSettings = mutableMapOf<String, String>()
        section.getConfigurationSection("game_settings")?.let { settingsSec ->
            for (key in settingsSec.getKeys(false)) {
                settingsSec.getString(key)?.let { value ->
                    gameSettings[key] = value
                }
            }
        }

        val requirements = section.getConfigurationSection("requirements")?.let { reqSec ->
            HarvestRequirements(
                permission = reqSec.getString("permission"),
                staminaCost = reqSec.getDouble("mmocore_stamina", 0.0),
                manaCost = reqSec.getDouble("mmocore_mana", 0.0),
                durabilityCost = reqSec.getInt("durability", 0)
            )
        } ?: HarvestRequirements()

        return HarvestTool(
            id = id,
            adapter = matcher,
            gameType = gameType,
            gameSettings = gameSettings,
            requirements = requirements,
        )
    }

    private fun loadCategory(id: String, section: ConfigurationSection): HarvestCategory? {
        val blockStrList = section.getStringList("blocks")
        val blocks = blockStrList.mapNotNull { Material.matchMaterial(it) }.toSet()

        if (blocks.isEmpty()) {
            plugin.logger.warning("카테고리 '$id' 로드 실패: 유효한 블록이 하나도 없습니다.")
            return null
        }

        val toolIdList = section.getStringList("allowed_tools").toSet()

        val optionSection = section.getConfigurationSection("options")
        val breakOnSuccess = optionSection?.getBoolean("break_on_success", true) ?: true
        val breakOnFailure = optionSection?.getBoolean("break_on_fail", true) ?: true

        val rewardSuccess = RewardFactory.parseList(section.getMapList("rewards_success"))
        val rewardFailure = RewardFactory.parseList(section.getMapList("rewards_fail"))

        return HarvestCategory(
            id = id,
            targetBlocks = blocks,
            allowedToolIds = toolIdList,
            doBreakOnSuccess = breakOnSuccess,
            doBreakOnFailure = breakOnFailure,
            successReward = rewardSuccess,
            failureReward = rewardFailure
        )
    }

    private fun loadYaml(fileName: String): FileConfiguration {
        val file = File(plugin.dataFolder, fileName)

        if (!file.exists()) {
            plugin.saveResource(fileName, false)
        }

        val config = YamlConfiguration.loadConfiguration(file)
        return config
    }

}