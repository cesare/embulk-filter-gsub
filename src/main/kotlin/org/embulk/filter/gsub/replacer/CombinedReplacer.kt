package org.embulk.filter.gsub.replacer

class CombinedReplacer(private val replacers: List<TextReplacer>) : TextReplacer {
    override fun execute(text: String): String {
        return replacers.fold(text, { str, replacer -> replacer.execute(str) })
    }
}
