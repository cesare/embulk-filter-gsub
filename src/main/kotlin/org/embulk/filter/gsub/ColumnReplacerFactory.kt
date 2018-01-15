package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.*

class ColumnReplacerFactory {
    fun create(task: GsubFilterPlugin.PluginTask): Map<String, TextReplacer> {
        return task.targetColumns.mapValues { column ->
            createReplacerForRules(column.value)
        }
    }

    private fun createReplacerForRules(rules: List<SubstitutionRule>): TextReplacer {
        val replacers = rules.map { createReplacer(it) }
        return CombinedReplacer(replacers)
    }

    private fun createReplacer(rule: SubstitutionRule): TextReplacer {
        return TextReplacerFactory.create(rule)
    }
}
