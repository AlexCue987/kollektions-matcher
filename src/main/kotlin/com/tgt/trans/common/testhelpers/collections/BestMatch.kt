package com.tgt.trans.common.testhelpers.collections

private fun sortByCriteria(matches: List<List<RangeMatch>>): List<List<RangeMatch>> {
    return matches.sortedWith(compareBy({ -countMatchedItems(it) },
            { -maxLengthOfMatchedRange(it) },
            { countMatchedRanges(it) },
            { it.count() })
    )
}

fun bestTwoMatches(matches: List<List<RangeMatch>>): List<List<RangeMatch>> {
    val sortedMatches = sortByCriteria(matches)
    return listOf(sortedMatches.firstOrNull { it[0].match },
            sortedMatches.firstOrNull { !it[0].match })
            .filter { it != null }
            .map { it!! }
}

fun countMatchedItems(matches: List<RangeMatch>): Int = matches.filter { it.match }.sumOf { it.leftRange.length() }

fun maxLengthOfMatchedRange(matches: List<RangeMatch>): Int {
    @Suppress("DEPRECATION")
    val maxLength = matches.filter { it.match }.maxOfOrNull { it.leftRange.length() }
    return maxLength ?: 0
}

fun countMatchedRanges(matches: List<RangeMatch>): Int = matches.count {it.match}

val defaultMismatch = listOf(RangeMatch(false, 0..-1, 0..-1))

fun bestMatch(matches: List<List<RangeMatch>>) =
        sortByCriteria(matches).firstOrNull() ?: defaultMismatch
