package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BestTwoMatchesTest: StringSpec() {

    val matches1 = listOf(
            RangeMatch(true, 1..3, 3..5),
            RangeMatch(false, 4..5, 6..5),
            RangeMatch(true, 6..9, 6..9)
    )

    val matches2 = listOf(
            RangeMatch(true, 1..3, 3..5),
            RangeMatch(false, 4..4, 6..5),
            RangeMatch(true, 5..5, 6..6),
            RangeMatch(false, 6..5, 7..8),
            RangeMatch(true, 6..7, 9..10)
    )

    val matches3 = listOf(
            RangeMatch(false, 0..0, 0..2),
            RangeMatch(true, 1..2, 3..4),
            RangeMatch(false, 3..4, 5..5),
            RangeMatch(true, 5..9, 6..10)
    )

    val matches4 = listOf(
            RangeMatch(false, 0..0, 0..2),
            RangeMatch(true, 1..2, 3..4),
            RangeMatch(false, 3..4, 5..5),
            RangeMatch(true, 5..5, 6..6),
            RangeMatch(false, 6..5, 7..8),
            RangeMatch(true, 6..9, 9..12)
    )

    init {
        "bestTwoMatches_handlesEmptyList" {
            val actual = bestTwoMatches(emptyList())
            actual.isEmpty() shouldBe true
        }

        "bestTwoMatches_onlyTailStartingWithMatch" {
            val actual = bestTwoMatches(listOf(matches1, matches2))
            actual shouldBe listOf(matches1)
        }

        "bestTwoMatches_onlyTailStartingWithMismatch" {
            val actual = bestTwoMatches(listOf(matches3, matches4))
            actual shouldBe listOf(matches3)
        }

        "bestTwoMatches_twoTails" {
            val actual = bestTwoMatches(listOf(matches1, matches2, matches3, matches4))
            actual shouldBe listOf(matches1, matches3)
        }
    }
}