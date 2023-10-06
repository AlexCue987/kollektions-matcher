package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ListsWithCustomMatchersTest: StringSpec() {
    private val sut = ListMatcher()

    init {
        "caseInsensitiveMatch" {
            val leftList = listOf("Green", "Blue", "Purple")
            val rightList = listOf("green", "BLUE", "PuRpLe")
            val actual =
                sut.match(leftList, rightList, matcher = { a: String, b: String -> a.lowercase() == b.lowercase() })
            val expected = listOf(RangeMatch(true, 0..2, 0..2))
            actual shouldBe expected
        }

        "caseInsensitiveMismatch" {
            val leftList = listOf("Green", "Blue", "Purple")
            val rightList = listOf("green", "BLUE", "PuRpLish")
            val actual =
                sut.match(leftList, rightList, matcher = { a: String, b: String -> a.lowercase() == b.lowercase() })
            val expected = listOf(
                RangeMatch(true, 0..1, 0..1),
                RangeMatch(false, 2..2, 2..2)
            )
            actual shouldBe expected
        }

        "matchOnOneField" {
            val leftList = listOf(RandomThing("Cucumber", 8), RandomThing("Zuccini", 10))
            val rightList = listOf(RandomThing("Pliers", 8), RandomThing("Hammer", 10))
            val actual =
                sut.match(leftList, rightList, matcher = { a: RandomThing, b: RandomThing -> a.length == b.length })
            val expected = listOf(RangeMatch(true, 0..1, 0..1))
            actual shouldBe expected
        }

        "mismatchOnOneField" {
            val leftList = listOf(RandomThing("Cucumber", 8), RandomThing("Zuccini", 9))
            val rightList = listOf(RandomThing("Pliers", 8), RandomThing("Hammer", 10))
            val actual =
                sut.match(leftList, rightList, matcher = { a: RandomThing, b: RandomThing -> a.length == b.length })
            val expected = listOf(
                RangeMatch(true, 0..0, 0..0),
                RangeMatch(false, 1..1, 1..1)
            )
            actual shouldBe expected
        }
    }
}

data class RandomThing(val randomName: String, val length: Int)