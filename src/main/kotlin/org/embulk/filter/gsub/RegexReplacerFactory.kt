package org.embulk.filter.gsub

import org.embulk.filter.gsub.replacer.RegexFactory
import org.embulk.filter.gsub.replacer.RegexOptionConfig
import org.embulk.filter.gsub.replacer.RegexReplacer
import org.embulk.filter.gsub.replacer.TextReplacer

class RegexReplacerFactory : TextReplacerFactory() {
    override fun create(rule: SubstitutionRule): TextReplacer {
        val pattern = rule.pattern.get()
        val to = rule.to.get()

        val regexOptionConfig = RegexOptionConfig()

        val factory = RegexFactory()
        val regex = factory.create(pattern, regexOptionConfig)

        return RegexReplacer(regex, to)
    }
}
