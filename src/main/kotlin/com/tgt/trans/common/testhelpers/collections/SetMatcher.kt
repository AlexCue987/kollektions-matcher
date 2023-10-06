package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.utils.joinNotEmptyToString

operator fun<T> Set<T>.plus( b: Set<T>) = { val allItems = mutableSetOf<T>()
    allItems.addAll(this)
    allItems.addAll(b)
 allItems }

fun<T> Set<T>.intersect( b: Set<T>): Set<T> {
    val allItems = mutableSetOf<T>()
    allItems.addAll(this.filter { b.contains(it) })
    return allItems
}

operator fun<T> Set<T>.minus( b: Set<T>) = this.filterNot { it in b }

fun<T> matchSets(expected: Set<T>,
                 actual: Set<T>,
                 itemDescriptor: (item: Any) -> String = { item -> item.toString() },
                 message: String = "") {
    val result = compareSets(expected, actual, itemDescriptor, message)
    result.assertSuccess()
}

fun <T> compareSets(expected: Set<T>, actual: Set<T>,
                            itemDescriptor: (item: Any) -> String = { item -> item.toString() },
                            message: String = ""): AssertionResult {
    val expectedNotActual = expected - actual
    val actualNotExpected = actual - expected

    return if (expectedNotActual.isEmpty() && actualNotExpected.isEmpty()) AssertionResult(true)
    else {
        val expectedNotActualDescription = describeList(expectedNotActual, "Expected, but not in actual:", itemDescriptor)
        val actualNotExpectedDescription = describeList(actualNotExpected, "Actual, but not in expected:", itemDescriptor)
        val possibleMatches = possibleMatchesDescription(expected, actual)
        val description = listOf(expectedNotActualDescription, actualNotExpectedDescription, possibleMatches)
                .joinNotEmptyToString(separator = "\n\n")
        AssertionResult(false, "${prefixIfNotEmpty(message)}$description")
    }
}

fun<T> possibleMatchesDescription(expected: Set<T>, actual: Set<T>): String {
    val possibleMatches = closestMatches(expected, actual)
    return if(possibleMatches.isEmpty()) ""
    else {
        "Possible Matches:\n${possibleMatches.joinToString("\n\n"){it.comparisonResult.description()}}"
    }
}

fun<T> describeList(items: List<T>, name: String, itemDescriptor: (item: Any) -> String) =
        with(StringBuilder(if(items.isNotEmpty()) name else "")){
    items.forEach { append("\n"); append(itemDescriptor(it as Any)) }
    toString()
}

fun prefixIfNotEmpty(s: String) = if (s.isNotEmpty()) "$s:\n" else ""

data class AssertionResult(val success: Boolean, val message: String = ""){
    fun assertSuccess(){
        if (!success) {
            throw AssertionError(message)
        }
    }
}

