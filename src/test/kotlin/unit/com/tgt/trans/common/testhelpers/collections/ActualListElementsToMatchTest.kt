package com.tgt.trans.common.testhelpers.collections

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ActualListElementsToMatchTest: StringSpec() {
    private val actual = listOf("apple", "orange", "lemon", "cherry")
    private val actualIndexed = actual.mapIndexed { index, it ->
        IndexedElement(index, it)
    }

    init {
        "actualListElementsToMatch returns nothing when no matches" {
            actualListElementsToMatch(
                elementMatches = listOf(
                    RangeMatch(
                        match = false,
                        leftRange = 0..2,
                        rightRange = 0..-1
                    )
                ),
                actual = actual
            ) shouldBe actualIndexed
        }

        "actualListElementsToMatch returns something when partial match" {
            actualListElementsToMatch(
                elementMatches = listOf(
                    RangeMatch(
                        match = true,
                        leftRange = 0..1,
                        rightRange = 0..1
                    ),
                    RangeMatch(
                        match = false,
                        leftRange = 2..3,
                        rightRange = 2..3
                    ),
                ),
                actual = actual
            ) shouldBe actualIndexed.takeLast(2)
        }

        "actualListElementsToMatch returns nothing when complete match" {
            actualListElementsToMatch(
                elementMatches = listOf(
                    RangeMatch(
                        match = true,
                        leftRange = 0..3,
                        rightRange = 0..3
                    )
                ),
                actual = actual
            ) shouldBe listOf()
        }
    }
}