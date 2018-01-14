package org.embulk.filter.gsub

import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.ConfigSource
import org.embulk.config.Task
import org.embulk.config.TaskSource
import org.embulk.spi.*

class GsubFilterPlugin : FilterPlugin {
    interface PluginTask : Task {
        @get:Config("target_columns")
        @get:ConfigDefault("{}")
        val targetColumns: Map<String, List<SubstitutionRule>>
    }

    override fun transaction(config: ConfigSource, inputSchema: Schema,
                             control: FilterPlugin.Control) {
        val task = config.loadConfig<PluginTask>(PluginTask::class.java)

        control.run(task.dump(), inputSchema)
    }

    override fun open(taskSource: TaskSource, inputSchema: Schema,
                      outputSchema: Schema, output: PageOutput): PageOutput {
        val task = taskSource.loadTask<PluginTask>(PluginTask::class.java)

        return object: PageOutput {
            val pageReader = PageReader(inputSchema)
            val pageBuilder = PageBuilder(Exec.getBufferAllocator(), outputSchema, output)
            val columnVisitor = ColumnVisitorImpl(task, pageReader, pageBuilder)

            override fun add(page: Page) {
                pageReader.setPage(page)

                while (pageReader.nextRecord()) {
                    inputSchema.visitColumns(columnVisitor)
                    pageBuilder.addRecord()
                }
            }

            override fun finish() {
                pageBuilder.finish()
            }

            override fun close() {
                pageBuilder.close()
            }
        }
    }
}
