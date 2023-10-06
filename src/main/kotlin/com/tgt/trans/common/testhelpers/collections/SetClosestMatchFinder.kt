package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.distance.*

fun<T>  closestMatches(expected: List<T>, actual: List<T>): List<PairComparison<T>> =
    closestMatches(expected.toSet(), actual.toSet())

fun<T>  closestMatches(expected: Set<T>, actual: Set<T>): List<PairComparison<T>> {
    return actual.filter { it !in expected }
        .flatMap { element ->
            expected.asSequence().mapNotNull { candidate ->
                val comparisonResult = VanillaDistanceCalculator.compare("", candidate, element)
                if (comparisonResult is MismatchByField &&
                    comparisonResult.distance.distance > Distance.COMPLETE_MISMATCH_VALUE) {
                    PairComparison(element, candidate, comparisonResult)
                } else null
            }.topWithTiesBy {
                it.comparisonResult.distance.distance
            }
         }
}

data class PairComparison<T>(
    val value: T,
    val possibleMatch: T,
    val comparisonResult: MismatchByField
)