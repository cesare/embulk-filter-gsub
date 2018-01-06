package org.embulk.filter.gsub

import com.google.common.base.Optional

import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.ConfigDiff
import org.embulk.config.ConfigSource
import org.embulk.config.Task
import org.embulk.config.TaskSource
import org.embulk.spi.Column
import org.embulk.spi.FilterPlugin
import org.embulk.spi.PageOutput
import org.embulk.spi.Schema

class GsubFilterPlugin : FilterPlugin {
    interface PluginTask : Task {
        // configuration option 1 (required integer)
        @get:Config("option1")
        val option1: Int

        // configuration option 2 (optional string, null is not allowed)
        @get:Config("option2")
        @get:ConfigDefault("\"myvalue\"")
        val option2: String

        // configuration option 3 (optional string, null is allowed)
        @get:Config("option3")
        @get:ConfigDefault("null")
        val option3: Optional<String>
    }

    override fun transaction(config: ConfigSource, inputSchema: Schema,
                             control: FilterPlugin.Control) {
        val task = config.loadConfig<PluginTask>(PluginTask::class.java)

        control.run(task.dump(), inputSchema)
    }

    override fun open(taskSource: TaskSource, inputSchema: Schema,
                      outputSchema: Schema, output: PageOutput): PageOutput {
        val task = taskSource.loadTask<PluginTask>(PluginTask::class.java)

        // Write your code here :)
        throw UnsupportedOperationException("GsubFilterPlugin.open method is not implemented yet")
    }
}
