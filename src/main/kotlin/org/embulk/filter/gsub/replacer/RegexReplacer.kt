package org.embulk.filter.gsub.replacer

class RegexReplacer(private val pattern: Regex, private val to: String) : TextReplacer {
    override fun execute(text: String): String {
        return pattern.replace(text, to)
    }
}
