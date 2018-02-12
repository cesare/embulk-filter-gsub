package org.embulk.filter.gsub.replacer

import org.embulk.config.TaskSource
import org.embulk.filter.gsub.RegexFactory
import org.embulk.filter.gsub.RegexOptions
import org.junit.Assert
import org.junit.Test

class RegexReplacerTest {
    @Test
    fun testExecute() {
        val regexOptions = createRegexpOption()

        val factory = RegexFactory()
        val pattern = factory.create("(\\w*):\\s*(.*)", regexOptions)

        val replacer = RegexReplacer(pattern, "$1 [$2]")
        val result = replacer.execute("test: foo bar baz")
        Assert.assertEquals("test [foo bar baz]", result)
    }

    @Test
    fun testExecuteWithoutIgnoreCaseOption() {
        val regexOptions = createRegexpOption(ignoreCase = false)

        val factory = RegexFactory()
        val pattern = factory.create("foo", regexOptions)

        val replacer = RegexReplacer(pattern, "*test-foo*")

        Assert.assertEquals("*test-foo* bar baz", replacer.execute("foo bar baz"))
        Assert.assertEquals("Foo bar baz", replacer.execute("Foo bar baz"))
        Assert.assertEquals("FOO bar baz", replacer.execute("FOO bar baz"))
    }

    @Test
    fun testExecuteWithIgnoreCaseOption() {
        val regexOptions = createRegexpOption(ignoreCase = true)
        val factory = RegexFactory()
        val pattern = factory.create("foo", regexOptions)

        val replacer = RegexReplacer(pattern, "*test-foo*")

        Assert.assertEquals("*test-foo* bar baz", replacer.execute("foo bar baz"))
        Assert.assertEquals("*test-foo* bar baz", replacer.execute("Foo bar baz"))
        Assert.assertEquals("*test-foo* bar baz", replacer.execute("FOO bar baz"))
    }

    @Test
    fun testExecuteWithoutMultilineOption() {
        val regexOptions = createRegexpOption(multiline = false)
        val factory = RegexFactory()
        val pattern = factory.create("^bar", regexOptions)

        val replacer = RegexReplacer(pattern, "*BAR*")

        Assert.assertEquals("foo\nbar\nbaz", replacer.execute("foo\nbar\nbaz"))
    }

    @Test
    fun testExecuteWithMultilineOption() {
        val regexOptions = createRegexpOption(multiline = true)
        val factory = RegexFactory()
        val pattern = factory.create("^bar", regexOptions)

        val replacer = RegexReplacer(pattern, "*BAR*")

        Assert.assertEquals("foo\n*BAR*\nbaz", replacer.execute("foo\nbar\nbaz"))
    }

    @Test
    fun testExecuteWithoutDotMatchesAllOption() {
        val regexOptions = createRegexpOption(dotMatchesAll = false)
        val factory = RegexFactory()
        val pattern = factory.create("foo.bar.baz", regexOptions)

        val replacer = RegexReplacer(pattern, "[foo-bar-baz]")

        Assert.assertEquals("[foo-bar-baz]", replacer.execute("foo/bar/baz"))
        Assert.assertEquals("foo\nbar/baz", replacer.execute("foo\nbar/baz"))
    }

    @Test
    fun testExecuteWithDotMatchesAllOption() {
        val regexOptions = createRegexpOption(dotMatchesAll = true)
        val factory = RegexFactory()
        val pattern = factory.create("foo.bar.baz", regexOptions)

        val replacer = RegexReplacer(pattern, "[foo-bar-baz]")

        Assert.assertEquals("[foo-bar-baz]", replacer.execute("foo/bar/baz"))
        Assert.assertEquals("[foo-bar-baz]", replacer.execute("foo\nbar/baz"))
    }

    @Test
    fun testExecuteWithEnableCommentsOption() {
        val regexOptions = createRegexpOption(enableComments = true)
        val factory = RegexFactory()

        val patternString = """
            |(ba\w)    # matches bar and baz
            """.trimMargin()
        val pattern = factory.create(patternString, regexOptions)

        val replacer = RegexReplacer(pattern, "*$1*")

        Assert.assertEquals("foo *bar* *baz*", replacer.execute("foo bar baz"))
    }

    private fun createRegexpOption(
            ignoreCase:     Boolean = false,
            multiline:      Boolean = true,
            dotMatchesAll:  Boolean = false,
            enableComments: Boolean = false
    ): RegexOptions {
        return object: RegexOptions {
            override val ignoreCase: Boolean
                get() = ignoreCase
            override val multiline: Boolean
                get() = multiline
            override val dotMatchesAll: Boolean
                get() = dotMatchesAll
            override val enableComments: Boolean
                get() = enableComments

            override fun validate() {
            }

            override fun dump(): TaskSource {
                throw NotImplementedError()
            }
        }
    }
}
