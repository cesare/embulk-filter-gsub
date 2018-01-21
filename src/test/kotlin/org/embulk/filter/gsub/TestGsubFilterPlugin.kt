package org.embulk.filter.gsub

import org.embulk.EmbulkTestRuntime
import org.embulk.config.ConfigLoader
import org.embulk.config.ConfigSource
import org.embulk.config.TaskSource
import org.embulk.spi.*
import org.embulk.spi.type.Types
import org.embulk.spi.util.Pages
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

import org.hamcrest.Matchers.*

class TestGsubFilterPlugin {
    @get:Rule
    val runtime = EmbulkTestRuntime()

    @Test
    fun testConfig() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  foo:
        |    - type: to_lower_case
        |      pattern: "[A-Z]*"
        |  bar:
        |    - type: "regexp_replace"
        |      pattern: "<br\\s*/?>"
        |      to: "\\n"
        |    - pattern: "(\\d+):(.*)"
        |      to: "\\1 [\\2]"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)
        val task = config.loadConfig(GsubFilterPlugin.PluginTask::class.java)

        val fooRules = task.targetColumns["foo"]
        Assert.assertThat(fooRules, hasSize(1))

        val fooRule = fooRules!![0]
        Assert.assertEquals("to_lower_case", fooRule.type)
        Assert.assertEquals("[A-Z]*", fooRule.pattern.get())

        val barRules = task.targetColumns["bar"]
        Assert.assertThat(barRules, hasSize(2))

        val barRule1 = barRules!![0]
        Assert.assertEquals("regexp_replace", barRule1.type)
        Assert.assertEquals("<br\\s*/?>", barRule1.pattern.get())
        Assert.assertEquals("\\n", barRule1.to.get())

        val barRule2 = barRules[1]
        Assert.assertEquals("regexp_replace", barRule2.type)
        Assert.assertEquals("(\\d+):(.*)", barRule2.pattern.get())
        Assert.assertEquals("\\1 [\\2]", barRule2.to.get())
    }

    @Test
    fun testDefaultRegexOptions() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  foo:
        |    - type: regexp_replace
        |      pattern: "test"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)
        val task = config.loadConfig(GsubFilterPlugin.PluginTask::class.java)
        val fooRules = task.targetColumns["foo"]!!
        val fooRule = fooRules[0]
        val regexOptions = fooRule.regexOptions
        Assert.assertFalse(regexOptions.ignoreCase)
        Assert.assertTrue(regexOptions.multiline)
        Assert.assertFalse(regexOptions.dotMatchesAll)
        Assert.assertFalse(regexOptions.enableComments)
    }

    @Test
    fun testRegexOptions() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  foo:
        |    - type: regexp_replace
        |      pattern: "test"
        |      regexp_options:
        |        ignore_case: true
        |        multiline: true
        |        dot_matches_all: true
        |        enable_comments: true
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)
        val task = config.loadConfig(GsubFilterPlugin.PluginTask::class.java)
        val fooRules = task.targetColumns["foo"]!!
        val fooRule = fooRules[0]
        val regexOptions = fooRule.regexOptions

        Assert.assertTrue(regexOptions.ignoreCase)
        Assert.assertTrue(regexOptions.multiline)
        Assert.assertTrue(regexOptions.dotMatchesAll)
        Assert.assertTrue(regexOptions.enableComments)
    }

    @Test
    fun testEmptyFilter() {
        val configYaml = """
        |type: gsub
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)

        val inputSchema = Schema.builder()
                .add("bool", Types.BOOLEAN)
                .add("long", Types.LONG)
                .add("string", Types.STRING)
                .build()

        val plugin = GsubFilterPlugin()
        plugin.transaction(config, inputSchema, object: FilterPlugin.Control {
            override fun run(taskSource: TaskSource, outputSchema: Schema) {
                val mockPageOutput = TestPageBuilderReader.MockPageOutput()
                val pageOutput = plugin.open(taskSource, inputSchema, outputSchema, mockPageOutput)

                val inputPage = PageTestUtils.buildPage(runtime.bufferAllocator, inputSchema, true, 1234L, "test for echo")
                for (page in inputPage) {
                    pageOutput.add(page)
                }
                pageOutput.finish()
                pageOutput.close()

                val records = Pages.toObjects(outputSchema, mockPageOutput.pages)
                val record = records.get(0)
                Assert.assertEquals(true, record[0])
                Assert.assertEquals(1234L, record[1])
                Assert.assertEquals("test for echo", record[2])
            }
        })
    }

    @Test
    fun testRegexReplaceFilter() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  string:
        |    - type: regexp_replace
        |      pattern: "test"
        |      to: "[replaced]"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)

        val inputSchema = Schema.builder()
                .add("bool", Types.BOOLEAN)
                .add("long", Types.LONG)
                .add("string", Types.STRING)
                .build()

        val plugin = GsubFilterPlugin()
        plugin.transaction(config, inputSchema, object: FilterPlugin.Control {
            override fun run(taskSource: TaskSource, outputSchema: Schema) {
                val mockPageOutput = TestPageBuilderReader.MockPageOutput()
                val pageOutput = plugin.open(taskSource, inputSchema, outputSchema, mockPageOutput)

                val inputPage = PageTestUtils.buildPage(runtime.bufferAllocator, inputSchema, true, 1234L, "test for echo")
                for (page in inputPage) {
                    pageOutput.add(page)
                }
                pageOutput.finish()
                pageOutput.close()

                val records = Pages.toObjects(outputSchema, mockPageOutput.pages)
                val record = records.get(0)
                Assert.assertEquals(true, record[0])
                Assert.assertEquals(1234L, record[1])
                Assert.assertEquals("[replaced] for echo", record[2])
            }
        })
    }

    @Test
    fun testToUpperCaseFilter() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  string:
        |    - type: to_upper_case
        |      pattern: "test"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)

        val inputSchema = Schema.builder()
                .add("bool", Types.BOOLEAN)
                .add("long", Types.LONG)
                .add("string", Types.STRING)
                .build()

        val plugin = GsubFilterPlugin()
        plugin.transaction(config, inputSchema, object: FilterPlugin.Control {
            override fun run(taskSource: TaskSource, outputSchema: Schema) {
                val mockPageOutput = TestPageBuilderReader.MockPageOutput()
                val pageOutput = plugin.open(taskSource, inputSchema, outputSchema, mockPageOutput)

                val inputPage = PageTestUtils.buildPage(runtime.bufferAllocator, inputSchema, true, 1234L, "test for echo")
                for (page in inputPage) {
                    pageOutput.add(page)
                }
                pageOutput.finish()
                pageOutput.close()

                val records = Pages.toObjects(outputSchema, mockPageOutput.pages)
                val record = records.get(0)
                Assert.assertEquals(true, record[0])
                Assert.assertEquals(1234L, record[1])
                Assert.assertEquals("TEST for echo", record[2])
            }
        })
    }

    @Test
    fun testToLowerCaseFilter() {
        val configYaml = """
        |type: gsub
        |target_columns:
        |  string:
        |    - type: to_lower_case
        |      pattern: "TEST"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)

        val inputSchema = Schema.builder()
                .add("bool", Types.BOOLEAN)
                .add("long", Types.LONG)
                .add("string", Types.STRING)
                .build()

        val plugin = GsubFilterPlugin()
        plugin.transaction(config, inputSchema, object: FilterPlugin.Control {
            override fun run(taskSource: TaskSource, outputSchema: Schema) {
                val mockPageOutput = TestPageBuilderReader.MockPageOutput()
                val pageOutput = plugin.open(taskSource, inputSchema, outputSchema, mockPageOutput)

                val inputPage = PageTestUtils.buildPage(runtime.bufferAllocator, inputSchema, true, 1234L, "TEST FOR ECHO")
                for (page in inputPage) {
                    pageOutput.add(page)
                }
                pageOutput.finish()
                pageOutput.close()

                val records = Pages.toObjects(outputSchema, mockPageOutput.pages)
                val record = records.get(0)
                Assert.assertEquals(true, record[0])
                Assert.assertEquals(1234L, record[1])
                Assert.assertEquals("test FOR ECHO", record[2])
            }
        })
    }

    private fun getConfigFromYaml(yaml: String): ConfigSource {
        val loader = ConfigLoader(Exec.getModelManager())
        return loader.fromYamlString(yaml)
    }
}
