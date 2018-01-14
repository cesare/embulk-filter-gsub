package org.embulk.filter.gsub

import org.embulk.filter.gsub.GsubFilterPlugin.PluginTask
import org.embulk.filter.gsub.replacer.TextReplacer
import org.embulk.spi.*

class ColumnVisitorImpl
    constructor(
        task: PluginTask,
        private val pageReader: PageReader,
        private val pageBuilder: PageBuilder
    ) : ColumnVisitor {

    private val columnReplacers: Map<String, TextReplacer> = ColumnReplacerFactory().create(task)

    override fun booleanColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            pageBuilder.setBoolean(column, pageReader.getBoolean(column))
        }
    }

    override fun longColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            pageBuilder.setLong(column, pageReader.getLong(column))
        }
    }

    override fun doubleColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            pageBuilder.setDouble(column, pageReader.getDouble(column))
        }
    }

    override fun stringColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            val text = pageReader.getString(column)
            val replacer = columnReplacers[column.name]
            val replacedText = replacer?.let { it.execute(text) } ?: text
            pageBuilder.setString(column, replacedText)
        }
    }

    override fun timestampColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            pageBuilder.setTimestamp(column, pageReader.getTimestamp(column))
        }
    }

    override fun jsonColumn(column: Column) {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column)
        }
        else {
            pageBuilder.setJson(column, pageReader.getJson(column))
        }
    }
}