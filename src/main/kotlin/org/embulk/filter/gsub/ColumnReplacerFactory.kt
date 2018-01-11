package org.embulk.filter.gsub

import org.embulk.filter.gsub.SubstitutionRule.SubstitutionType
import org.embulk.filter.gsub.replacer.*

class ColumnReplacerFactory {
    fun create(columns: List<TargetColumnConfig>): Map<String, TextReplacer> {
        val map = HashMap<String, TextReplacer>()
        columns.forEach { column -> map.put(column.name, createReplacerForColumn(column)) }
        return map
    }

    fun createReplacerForColumn(config: TargetColumnConfig): TextReplacer {
        val replacers = ArrayList<TextReplacer>()
        val combinedReplacer = CombinedReplacer(replacers)
        return combinedReplacer
    }

    private fun createReplacer(rule: SubstitutionRule): TextReplacer {
        val type = findSubstitutionType(rule.type)
        when (type) {
            SubstitutionType.REGEXP_REPLACE -> return createRegexReplacer(rule)
            SubstitutionType.TO_LOWER_CASE -> return createLowerCaseReplacer(rule)
            else -> throw RuntimeException("Substitution type ${type.label} is not supported")
        }
    }

    private fun findSubstitutionType(typeName: String): SubstitutionType {
        try {
            return SubstitutionType.valueOf(typeName)
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
