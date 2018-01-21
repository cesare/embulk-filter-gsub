package org.embulk.filter.gsub.replacer

import org.embulk.config.TaskSource
import org.embulk.filter.gsub.RegexFactory
import org.embulk.filter.gsub.RegexOptions
import org.junit.Assert
import org.junit.Test

class RegexReplacerTest {
    @Test
    fun testExecute() {
        val regexOptions = object: RegexOptions {
            override val ignoreCase: Boolean
                get() = true
            override val multiline: Boolean
                get() = true
            override val dotMatchesAll: Boolean
                get() = false
            override val enableComments: Boolean
                get() = false

            override fun validate() {
            }

            override fun dump(): TaskSource {
                throw NotImplementedError()
            }
        }

        val factory = RegexFactory()
        val pattern = factory.create("(\\w*):\\s*(.*)", regexOptions)

        val replacer = RegexReplacer(pattern, "$1 [$2]")
        val result = replacer.execute("test: foo bar baz")
        Assert.assertEquals("test [foo bar baz]", result)
    }
}
