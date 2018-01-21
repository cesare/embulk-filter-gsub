package org.embulk.filter.gsub

import org.embulk.config.Config
import org.embulk.config.ConfigDefault
import org.embulk.config.Task

interface RegexOptions : Task {
    @get:Config("ignore_case")
    @get:ConfigDefault("false")
    val ignoreCase: Boolean

    @get:Config("multiline")
    @get:ConfigDefault("true")
    val multiline: Boolean

    @get:Config("dot_matches_all")
    @get:ConfigDefault("false")
    val dotMatchesAll: Boolean

    @get:Config("enable_comments")
    @get:ConfigDefault("false")
    val enableComments: Boolean
}