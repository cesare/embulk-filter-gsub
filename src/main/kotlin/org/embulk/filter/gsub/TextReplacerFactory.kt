package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.TextReplacer
import org.embulk.filter.gsub.SubstitutionRule.SubstitutionType

abstract class TextReplacerFactory {
    abstract fun create(rule: SubstitutionRule): TextReplacer

    companion object Factory {
        fun create(rule: SubstitutionRule): TextReplacer {
            val factory = createFactory(rule)
            return factory.create(rule)
        }

        private fun createFactory(rule: SubstitutionRule): TextReplacerFactory {
            val type = findSubstitutionType(rule.type)
            return when (type) {
                SubstitutionType.REGEXP_REPLACE -> RegexReplacerFactory()
                SubstitutionType.TO_UPPER_CASE -> UpperCaseReplacerFactory()
                SubstitutionType.TO_LOWER_CASE -> LowerCaseReplacerFactory()
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
    }
}
