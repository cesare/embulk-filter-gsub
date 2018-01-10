package org.embulk.filter.gsub

import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.Task

interface TargetColumnConfig : Task {
    @get:Config("name")
    val name: String

    @get:Config("rules")
    @get:ConfigDefault("[]")
    val rules: List<SubstitutionRule>
}
