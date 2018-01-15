package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.LowerCaseReplacer
import org.embulk.filter.gsub.replacer.RegexFactory
import org.embulk.filter.gsub.replacer.RegexOptionConfig
import org.embulk.filter.gsub.replacer.TextReplacer

class LowerCaseReplacerFactory : TextReplacerFactory() {
    override fun create(rule: SubstitutionRule): TextReplacer {
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