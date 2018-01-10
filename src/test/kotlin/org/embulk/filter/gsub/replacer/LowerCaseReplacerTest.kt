package org.embulk.filter.gsub.replacer

import org.junit.Assert
import org.junit.Test

class LowerCaseReplacerTest {
    @Test
    fun testExecuteWithoutPattern() {
        val replacer = LowerCaseReplacer()
        val result = replacer.execute("Foo Bar BAZ")
        Assert.assertEquals("foo bar baz", result)
    }

    @Test
    fun testExecuteWithPattern() {
        val replacer = LowerCaseReplacer("[A-Z]{3}".toRegex())
        val result = replacer.execute("Foo Bar BAZ")
        Assert.assertEquals("Foo Bar baz", result)
    }
}
