package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BestMatchTest: StringSpec() {
    val matches1 = listOf(
            RangeMatch(true, 1..3, 3..5),
            RangeMatch(false, 4..5, 6..5),
            RangeMatch(true, 6..9, 6..9)
    )

    val noMatches = listOf(
            RangeMatch(false, 0..2, 0..3)
    )

    val matches2 = listOf(
            RangeMatch(true, 1..3, 3..5),
            RangeMatch(false, 4..4, 6..5),
            RangeMatch(true, 5..5, 6..6),
            RangeMatch(false, 6..5, 7..8),
            RangeMatch(true, 6..7, 9..10)
    )

    val matches3 = listOf(
            RangeMatch(true, 1..2, 3..4),
            RangeMatch(false, 3..4, 5..5),
            RangeMatch(true, 5..9, 6..10)
    )

    val matches4 = listOf(
            RangeMatch(true, 1..2, 3..4),
            RangeMatch(false, 3..4, 5..5),
            RangeMatch(true, 5..5, 6..6),
            RangeMatch(false, 6..5, 7..8),
            RangeMatch(true, 6..9, 9..12)
    )

    val matches5 = listOf(
            RangeMatch(false, 1..0, 3..2),
            RangeMatch(true, 1..3, 3..5),
            RangeMatch(false, 4..5, 6..5),
            RangeMatch(true, 6..9, 6..9)
    )

    val matches6 = listOf(
            RangeMatch(false, 1..0, 3..2),
            RangeMatch(true, 1..2, 3..4),
            RangeMatch(false, 3..4, 5..5),
            RangeMatch(true, 5..9, 6..10)
    )

    init {
    "countMatchedItems" {
        val actual = countMatchedItems(matches1)
        actual shouldBe 7
    }

    "countMatchedItems_noMatches" {
        val actual = countMatchedItems(noMatches)
        actual shouldBe 0
    }

    "maxLengthOfMatchedRange" {
        val actual = maxLengthOfMatchedRange(matches1)
        actual shouldBe 4
    }

    "maxLengthOfMatchedRange_noMatches" {
        val actual = maxLengthOfMatchedRange(noMatches)
        actual shouldBe 0
    }

    "countMatchedRanges" {
        val actual = countMatchedRanges(matches1)
        actual shouldBe 2
    }

    "countMatchedRanges_noMatch" {
        val actual = countMatchedRanges(noMatches)
        actual shouldBe 0
    }

    "bestMatch_chooseTailWithMostMatchedItems" {
        withClue("Guardian assumption") {
            (countMatchedItems(matches1) > countMatchedItems(matches2)) shouldBe true
        }
            val actual = bestMatch(listOf(matches1, matches2))
            actual shouldBe matches1
        }
        
    "bestMatch_chooseTailWithLongestMatchedRange" {
        withClue("Guardian assumption 1"){
            (countMatchedItems(matches1) == countMatchedItems(matches3)) shouldBe true
        }
            withClue("Guardian assumption 2") {
                (maxLengthOfMatchedRange(matches1) < maxLengthOfMatchedRange(matches3)) shouldBe true
            }
            val actual = bestMatch(listOf(matches1, matches3))
            actual shouldBe matches3
        }

    "bestMatch_chooseTailWithLeastMatchedRanges" {
        withClue("Guardian assumption 1") {
            (countMatchedItems(matches1) == countMatchedItems(matches4)) shouldBe true
        }
        withClue("Guardian assumption 2") {
            (maxLengthOfMatchedRange(matches1) == maxLengthOfMatchedRange(matches4)) shouldBe true
        }
        withClue("Guardian assumption 3") {
            (countMatchedRanges(matches1) < countMatchedRanges(matches4)) shouldBe true
        }
        val actual = bestMatch(listOf(matches1, matches4))
        actual shouldBe matches1
    }

    "bestMatch_chooseTailWithLeastRanges" {
        withClue("Guardian assumption 1") {
            (countMatchedItems(matches1) == countMatchedItems(matches5)) shouldBe true
        }
        withClue("Guardian assumption 2") {
            (maxLengthOfMatchedRange(matches1) == maxLengthOfMatchedRange(matches5))  shouldBe true
        }
            withClue("Guardian assumption 3") {
                (countMatchedRanges(matches1) == countMatchedRanges(matches5)) shouldBe true
            }
            val actual = bestMatch(listOf(matches1, matches5))
            actual shouldBe matches1
        }

    "bestMatch_handlesNoMatches" {
        val actual = bestMatch(listOf(noMatches))
        actual shouldBe noMatches
    }

    "bestTwoMatches_allTailsStartWithMatch" {
        val matches = listOf(matches1, matches2, matches3)
        withClue("Guardian assumption") { bestMatch(matches) shouldBe matches3 }
        val actual = bestTwoMatches(matches)
        actual shouldBe listOf(matches3)
    }

   "bestTwoMatches_allTailsStartWithMismatch" {
        val matches = listOf(matches5, matches6)
        withClue("Guardian assumption") { bestMatch(matches) shouldBe matches6 }
        val actual = bestTwoMatches(matches)
        actual shouldBe listOf(matches6)
    }

    "bestTwoMatches" {
        val matches = listOf(matches1, matches2, matches3, matches4, matches5, matches6)
        val actual = bestTwoMatches(matches)
        actual shouldBe listOf(matches3, matches6)
    }
    }
}