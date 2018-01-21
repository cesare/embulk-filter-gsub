package org.embulk.filter.gsub

import org.embulk.filter.gsub.RegexOptions

class RegexFactory {
    fun create(patternString: String, regexOptions: RegexOptions): Regex {
        val options = buildOptions(regexOptions)
        return Regex(patternString, options)
    }

    private fun buildOptions(regexOptions: RegexOptions): Set<RegexOption> {
        val options = HashSet<RegexOption>()

        if (regexOptions.ignoreCase) {
            options.add(RegexOption.IGNORE_CASE)
        }

        if (regexOptions.multiline) {
            options.add(RegexOption.MULTILINE)
        }

        if (regexOptions.dotMatchesAll) {
            options.add(RegexOption.DOT_MATCHES_ALL)
        }

        if (regexOptions.enableComments) {
            options.add(RegexOption.COMMENTS)
        }

        return options
    }
}
