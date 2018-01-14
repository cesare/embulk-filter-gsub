package org.embulk.filter.gsub

import org.embulk.filter.gsub.SubstitutionRule.SubstitutionType
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
        val type = findSubstitutionType(rule.type)
        return when (type) {
            SubstitutionType.REGEXP_REPLACE -> createRegexReplacer(rule)
            SubstitutionType.TO_LOWER_CASE -> createLowerCaseReplacer(rule)
            else -> throw RuntimeException("Substitution type ${type.label} is not supported")
        }
    }

    private fun findSubstitutionType(typeName: String): SubstitutionType {
        try {
            return SubstitutionType.valueOf(typeName.toUpperCase())
        }
        catch (e: IllegalArgumentException) {
            throw RuntimeException("Unknown substitution type: ${typeName}", e)
        }
    }

    private fun createRegexReplacer(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.get()
        val to = rule.to.get()

        val regexOptionConfig = RegexOptionConfig()

        val factory = RegexFactory()
        val regex = factory.create(pattern, regexOptionConfig)

        return RegexReplacer(regex, to)
    }

    private fun createLowerCaseReplacer(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.orNull()
        if (pattern != null) {
            // TODO set regex options
            val regexOptionConfig = RegexOptionConfig()
            val factory = RegexFactory()
            val regex = factory.create(pattern, regexOptionConfig)

            return LowerCaseReplacer(regex)
        }
        else {
            return LowerCaseReplacer()
        }
    }
}
