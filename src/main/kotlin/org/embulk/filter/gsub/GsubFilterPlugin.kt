package org.embulk.filter.gsub

import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.ConfigSource
import org.embulk.config.Task
import org.embulk.config.TaskSource
import org.embulk.spi.FilterPlugin
import org.embulk.spi.PageOutput
import org.embulk.spi.Schema

class GsubFilterPlugin : FilterPlugin {
    interface PluginTask : Task {
        @get:Config("target_columns")
        @get:ConfigDefault("[]")
        val targetColumns: List<TargetColumnConfig>
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
