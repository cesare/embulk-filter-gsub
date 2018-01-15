package org.embulk.filter.gsub.replacer

class UpperCaseReplacer(private val pattern: Regex?) : TextReplacer {
    constructor() : this(null)

    override fun execute(text: String): String {
        return pattern?.let { replaceWithPattern(it, text)} ?: replaceWholeText(text)
    }

    private fun replaceWithPattern(pattern: Regex, text: String): String {
        return pattern.replace(text, { matchResult -> matchResult.value.toUpperCase() })
    }

    private fun replaceWholeText(text: String): String {
        return text.toUpperCase()
    }
}

