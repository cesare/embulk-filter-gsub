package org.embulk.filter.gsub.replacer

import org.junit.Assert
import org.junit.Test

class RegexReplacerTest {
    @Test
    fun testExecute() {
        val optionConfig = RegexOptionConfig()
        optionConfig.ignoreCase = true

        val factory = RegexFactory()
        val pattern = factory.create("(\\w*):\\s*(.*)", optionConfig)

        val replacer = RegexReplacer(pattern, "$1 [$2]")
        val result = replacer.execute("test: foo bar baz")
        Assert.assertEquals("test [foo bar baz]", result)
    }
}
