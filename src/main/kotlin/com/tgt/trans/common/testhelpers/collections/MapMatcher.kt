package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.utils.joinNotEmptyToString

fun<K, V> matchMaps(expected: Map<K, V>, actual: Map<K, V>,
                    keyDescriptor: (key: Any) -> String = { item -> item.toString() },
                    valueDescriptor: (value: Any?) -> String = { item -> item?.toString() ?: "null" },
                    valuesMatcher: (left: Any, right: Any) -> Boolean = {left, right -> left == right},
                    message: String = "") {
    val result = compareMaps(expected, actual, keyDescriptor, valueDescriptor, valuesMatcher, message)
    result.assertSuccess()
}

private fun <K, V> StringBuilder.describeMismatchedAny(valueDescriptor: (value: V?) -> String,
                                                       it: Map.Entry<K, Pair<V?, V?>>) {
    append("\n  expected=")
    append(valueDescriptor(it.value.first))
    append("\n  actual  =")
    append(valueDescriptor(it.value.second))
}

fun compareMaps(expected: Map<*, *>, actual: Map<*, *>,
                keyDescriptor: (key: Any) -> String = { item -> item.toString() },
                valueDescriptor: (value: Any?) -> String = { item -> item.toString() },
                valuesMatcher: (left: Any, right: Any) -> Boolean = {left, right -> left == right},
                message: String = ""): AssertionResult {
    val expectedNotActual = (expected.keys - actual.keys).associate { it to expected[it]!! }
    val actualNotExpected = (actual.keys - expected.keys).associate { it to actual[it]!! }
    val mismatchedItems = expected.keys.intersect(actual.keys)
            .filter { !nullableMatch(expected[it], actual[it], valuesMatcher) }
            .associate { it!! to Pair(expected[it], actual[it]) }
    if (expectedNotActual.isEmpty() && actualNotExpected.isEmpty() && mismatchedItems.isEmpty()) {
        return AssertionResult(true)
    }
    val expectedNotActualDescription = describeMap(expectedNotActual, "Key in expected, but not in actual:", keyDescriptor, valueDescriptor)
    val actualNotExpectedDescription = describeMap(actualNotExpected, "Key in actual, but not in expected:", keyDescriptor, valueDescriptor)
    val valuesMismatchDescription = describeMapOfMismatches(mismatchedItems, "Values mismatch:", keyDescriptor, valueDescriptor)
    val description = listOf(message,
            expectedNotActualDescription,
            actualNotExpectedDescription,
            valuesMismatchDescription)
            .joinNotEmptyToString("\n\n")
    return AssertionResult(false, description)
}

fun describeMap(items: Map<*, *>,
                name: String,
                keyDescriptor: (key: Any) -> String,
                valueDescriptor: (value: Any) -> String) =
        with(StringBuilder(if(items.isNotEmpty()) name else "")){
            items.forEach { append("\nkey=")
                append(keyDescriptor(it.key!!))
                append(", value=")
                append(valueDescriptor(it.value!!))
            }
            toString()
        }

fun describeMapOfMismatches(items: Map<Any, Pair<Any?, Any?>>, name: String,
                            keyDescriptor: (key: Any) -> String,
                            valueDescriptor: (value: Any?) -> String) =
        with(StringBuilder(if(items.isNotEmpty()) name else "")) {
            items.forEach {
                append("\nkey=")
                append(keyDescriptor(it.key))
                val expected = it.value.first
                val actual = it.value.second
                when {
                    expected is Map<*, *> && actual is Map<*, *> ->
                        append(prependLines("\n${compareMaps(expected, actual, valueDescriptor).message}", prefix = "  "))
                    expected is List<*> && actual is List<*> ->
                        append(prependLines("\n${compareLists(expected, actual).message}", prefix = "  "))
                    expected is Set<*> && actual is Set<*> ->
                        append(prependLines("\n${compareSets(expected, actual, itemDescriptor = valueDescriptor).message}", prefix = "  "))
                    else -> describeMismatchedAny(valueDescriptor, it)
                }

            }
            toString()
        }

fun describeMismatchedMaps(expected: Map<*, *>, actual: Map<*, *>): String {
    val result = compareMaps(expected, actual)
    return result.message
}

fun prependLines(linesStr: String, prefix: String): String {
    val lines  = linesStr.split("\n")
    return lines.joinToString("\n") {"$prefix$it".trimEnd()}
}

fun nullableMatch(a: Any?, b: Any?,
                  valuesMatcher: (left: Any, right: Any) -> Boolean = {
    left: Any, right: Any -> left == right
}): Boolean =
    when {
        a == b -> true
        a == null || b == null -> false
        else -> valuesMatcher(a, b)
    }