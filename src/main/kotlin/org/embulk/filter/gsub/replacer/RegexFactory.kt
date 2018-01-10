package org.embulk.filter.gsub.replacer

class RegexFactory {
    fun create(patternString: String, regexOptionConfig: RegexOptionConfig): Regex {
        val options = buildOptions(regexOptionConfig)
        return Regex(patternString, options)
    }

    private fun buildOptions(optionConfig: RegexOptionConfig): Set<RegexOption> {
        val options = HashSet<RegexOption>()

        if (optionConfig.ignoreCase) {
            options.add(RegexOption.IGNORE_CASE)
        }

        if (optionConfig.multiline) {
            options.add(RegexOption.MULTILINE)
        }

        return options
    }
}
