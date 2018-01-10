package org.embulk.filter.gsub.replacer

interface TextReplacer {
    fun execute(text: String): String
}
