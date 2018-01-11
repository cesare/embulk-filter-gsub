package org.embulk.filter.gsub.replacer

import org.junit.Assert
import org.junit.Test

class CombinedReplacerTest {
    @Test
    fun testExecuteWithEmptyReplacer() {
        val replacer = CombinedReplacer(ArrayList<TextReplacer>())
        val result = replacer.execute("foo bar baz")
        Assert.assertEquals("foo bar baz", result)
    }

    @Test
    fun testExecuteWithSingleReplacer() {
        val lowerCaseReplacer = LowerCaseReplacer()
        val replacers = ArrayList<TextReplacer>()
        replacers.add(lowerCaseReplacer)

        val combinedReplacer = CombinedReplacer(replacers)
        val result = combinedReplacer.execute("FOO BAR BAZ")
        Assert.assertEquals("foo bar baz", result)
    }

    @Test
    fun testExecuteWithMultipleReplacer() {
        val lowerCaseReplacer = LowerCaseReplacer()
        val regexReplacer = RegexReplacer("\\s+".toRegex(), "-")
        val replacers = ArrayList<TextReplacer>()
        replacers.add(lowerCaseReplacer)
        replacers.add(regexReplacer)

        val combinedReplacer = CombinedReplacer(replacers)
        val result = combinedReplacer.execute("FOO BAR BAZ")
        Assert.assertEquals("foo-bar-baz", result)
    }
}
