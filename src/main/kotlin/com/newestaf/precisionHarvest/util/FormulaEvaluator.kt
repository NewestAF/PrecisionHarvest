package com.newestaf.precisionHarvest.util

import me.clip.placeholderapi.PlaceholderAPI
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Functions
import org.bukkit.entity.Player

object FormulaEvaluator {

    fun evaluateInt(player: Player, formula: String): Int {
        return evaluate(player, formula).toInt()
    }

    fun evaluate(player: Player, formula: String): Double {
        if (formula.isBlank()) return 0.0

        val parsedFormula = PlaceholderAPI.setPlaceholders(player, formula)

        return try {
                ExpressionBuilder(parsedFormula)
                    .build()
                    .evaluate()
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }
}