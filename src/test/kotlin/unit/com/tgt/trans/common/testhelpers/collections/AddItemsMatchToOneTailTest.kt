package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AddItemsMatchToOneTailTest: StringSpec() {
    private val sut = ListMatcher()

    init {
        "blowsUpForEmptyTail" {
            val match = ItemsMatch(true, MatchType.BOTH)
            shouldThrow<IllegalArgumentException> {
                sut.addItemsMatchToOneTail(match, mutableListOf())
            }
        }

        "extendsMatch" {
            val match = ItemsMatch(true, MatchType.BOTH)
            val lastRangeMatch = RangeMatch(false, 3..4, 4..3)
            val tail = mutableListOf(RangeMatch(true, 1..2, 2..3), lastRangeMatch)
            val actual = sut.addItemsMatchToOneTail(match, tail)
            val expected = mutableListOf(RangeMatch(true, 0..2, 1..3), lastRangeMatch)
            actual shouldBe expected
        }

        "extendsMismatch" {
            val match = ItemsMatch(false, MatchType.BOTH)
            val lastRangeMatch = RangeMatch(true, 3..4, 4..5)
            val tail = mutableListOf(RangeMatch(false, 1..2, 2..3), lastRangeMatch)
            val actual = sut.addItemsMatchToOneTail(match, tail)
            val expected = mutableListOf(RangeMatch(false, 0..2, 1..3), lastRangeMatch)
            actual shouldBe expected
        }

        "addsMismatchBeforeMatch" {
            val match = ItemsMatch(false, MatchType.BOTH)
            val lastMatchInTail = RangeMatch(false, 3..4, 4..3)
            val firstMatchInTail = RangeMatch(true, 1..2, 2..3)
            val tail = mutableListOf(firstMatchInTail, lastMatchInTail)
            val actual = sut.addItemsMatchToOneTail(match, tail)
            val expected = mutableListOf(RangeMatch(false, 0..0, 1..1), firstMatchInTail, lastMatchInTail)
            actual shouldBe expected
        }

        "addsMmatchBeforeMismatch" {
            val match = ItemsMatch(true, MatchType.BOTH)
            val lastMatchInTail = RangeMatch(true, 3..4, 4..5)
            val firstMatchInTail = RangeMatch(false, 1..2, 2..3)
            val tail = mutableListOf(firstMatchInTail, lastMatchInTail)
            val actual = sut.addItemsMatchToOneTail(match, tail)
            val expected = mutableListOf(RangeMatch(true, 0..0, 1..1), firstMatchInTail, lastMatchInTail)
            actual shouldBe expected
        }
    }
}