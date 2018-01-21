package org.embulk.filter.gsub

import com.google.common.base.Optional
import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.Task

interface SubstitutionRule : Task {
    enum class SubstitutionType(val label: String) {
        REGEXP_REPLACE("regexp_replace"),
        TO_UPPER_CASE("to_upper_case"),
        TO_LOWER_CASE("to_lower_case"),
    }

    @get:Config("type")
    @get:ConfigDefault("\"regexp_replace\"")
    val type: String

    @get:Config("pattern")
    @get:ConfigDefault("null")
    val pattern: Optional<String>

    @get:Config("to")
    @get:ConfigDefault("null")
    val to: Optional<String>

    @get:Config("regexp_options")
    @get:ConfigDefault("{}")
    val regexOptions: RegexOptions
}
