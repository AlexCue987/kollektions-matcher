package com.tgt.trans.common.testhelpers.collections

fun<T> matchElementsIgnoringOrder(
    expected: Collection<T>,
    actual: Collection<T>,
    valueDescriptor: (value: Any?) -> String = { item -> item?.toString() ?: "null" },
    message: String = ""
) {
    val result = compareMaps(
        expected = groupAndCount(expected),
        actual = groupAndCount(actual),
        valueDescriptor = valueDescriptor,
        message = message
    )
    val detailedMessage = if(!result.success) {
        val possibleMatches = possibleMatchesDescription(expected.toSet(), actual.toSet())
        if(possibleMatches.isEmpty()) {
            result.message
        } else {
            "${result.message}\n\n$possibleMatches"
        }
    } else {
        result.message
    }
    result.copy(message = detailedMessage).assertSuccess()
}

fun<T> groupAndCount(elements: Collection<T>): Map<T, Int> =
    elements.groupingBy { it }.eachCount()