package org.embulk.filter.gsub

import org.embulk.EmbulkTestRuntime
import org.embulk.config.ConfigLoader
import org.embulk.config.ConfigSource
import org.embulk.spi.Exec
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

        val barRule2 = barRules!![1]
        Assert.assertEquals("regexp_replace", barRule2.type)
        Assert.assertEquals("(\\d+):(.*)", barRule2.pattern.get())
        Assert.assertEquals("\\1 [\\2]", barRule2.to.get())
    }

    fun getConfigFromYaml(yaml: String): ConfigSource {
        val loader = ConfigLoader(Exec.getModelManager())
        return loader.fromYamlString(yaml)
    }
}
