package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.RegexOptionConfig
import org.embulk.filter.gsub.replacer.RegexReplacer
import org.embulk.filter.gsub.replacer.TextReplacer

class RegexReplacerFactory : TextReplacerFactory() {
    override fun create(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.get()
        val to = rule.to.get()

        val factory = RegexFactory()
        val regex = factory.create(pattern, rule.regexOptions)

        return RegexReplacer(regex, to)
    }
}
