package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.LowerCaseReplacer
import org.embulk.filter.gsub.replacer.RegexOptionConfig
import org.embulk.filter.gsub.replacer.TextReplacer

class LowerCaseReplacerFactory : TextReplacerFactory() {
    override fun create(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.orNull()
        if (pattern != null) {
            val factory = RegexFactory()
            val regex = factory.create(pattern, rule.regexOptions)

            return LowerCaseReplacer(regex)
        }
        else {
            return LowerCaseReplacer()
        }
    }
}
