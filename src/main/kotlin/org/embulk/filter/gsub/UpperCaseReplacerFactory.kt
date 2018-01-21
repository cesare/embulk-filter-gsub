package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.RegexOptionConfig
import org.embulk.filter.gsub.replacer.TextReplacer
import org.embulk.filter.gsub.replacer.UpperCaseReplacer

class UpperCaseReplacerFactory : TextReplacerFactory() {
    override fun create(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.orNull()
        if (pattern != null) {
            val factory = RegexFactory()
            val regex = factory.create(pattern, rule.regexOptions)

            return UpperCaseReplacer(regex)
        }
        else {
            return UpperCaseReplacer()
        }
    }
}
