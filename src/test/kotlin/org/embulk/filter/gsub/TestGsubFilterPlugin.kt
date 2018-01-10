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
        |  - name: foo
        |    rules:
        |      - type: to_lower_case
        |        pattern: "[A-Z]*"
        |  - name: bar
        |    rules:
        |      - type: "regexp_replace"
        |        pattern: "<br\\s*/?>"
        |        to: "\\n"
        |      - pattern: "(\\d+):(.*)"
        |        to: "\\1 [\\2]"
        """.trimMargin()

        val config = getConfigFromYaml(configYaml)
        val task = config.loadConfig(GsubFilterPlugin.PluginTask::class.java)

        val fooColumn = task.targetColumns[0]
        Assert.assertEquals("foo", fooColumn.name)

        val fooRules = fooColumn!!.rules
        Assert.assertThat(fooRules, hasSize(1))

        val fooRule = fooRules[0]
        Assert.assertEquals("to_lower_case", fooRule?.type)
        Assert.assertEquals("[A-Z]*", fooRule?.pattern.get())

        val barColumn = task.targetColumns[1]
        Assert.assertEquals("bar", barColumn.name)

        val barRules = barColumn!!.rules
        Assert.assertThat(barRules, hasSize(2))

        val barRule1 = barRules[0]
        Assert.assertEquals("regexp_replace", barRule1.type)
        Assert.assertEquals("<br\\s*/?>", barRule1.pattern.get())
        Assert.assertEquals("\\n", barRule1.to.get())

    }

    fun getConfigFromYaml(yaml: String): ConfigSource {
        val loader = ConfigLoader(Exec.getModelManager())
        return loader.fromYamlString(yaml)
    }
}
