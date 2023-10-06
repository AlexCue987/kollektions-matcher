package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.temporal.ChronoUnit

class ListsTest: StringSpec() {
    private val sut = ListMatcher()

    val leftList = listOf("Cover Letter for TPS Report",
            "TPS Report, Page 1",
            "TPS Report, Page 2")
    val rightList = listOf("TPS Report, Page 1",
            "TPS Report, Page 2",
            "Cover Letter for TPS Report")
    val shortLeftList = listOf(
            "TPS Report, Page 1",
            "TPS Report, Page 2")

    init {
        "whenBothSublistsOver" {
            val endedLeftSubList = SubList(leftList, leftList.size)
            val endedRightSubList = SubList(rightList, rightList.size)
            val actual = sut.matches(endedLeftSubList, endedRightSubList, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(false, 3..2, 3..2)))
            actual shouldBe expected
        }

        "whenBothSublistsOnLastItemAndMatch" {
            val lastItemOfLeftList = SubList(leftList, leftList.size - 1)
            val lastItemOfShortLeftList = SubList(shortLeftList, shortLeftList.size - 1)
            val actual = sut.matches(lastItemOfLeftList, lastItemOfShortLeftList, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(true, 2..2, 1..1)))
            actual shouldBe expected
        }

        "whenBothSublistsOnLastItemAndMismatch" {
            val lastItemOfLeftList = SubList(leftList, leftList.size - 1)
            val lastItemOfRightList = SubList(rightList, rightList.size - 1)
            val actual = sut.matches(lastItemOfLeftList, lastItemOfRightList, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(false, 2..2, 2..2)))
            actual shouldBe expected
        }

        "whenLeftSublistOver" {
            val endedLeftSubList = SubList(leftList, leftList.size)
            val rightSubList = SubList(rightList, rightList.size - 1)
            val actual = sut.matches(endedLeftSubList, rightSubList, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(false, 3..2, 2..2)))
            actual shouldBe expected
        }

        "whenRightSublistOver" {
            val endedLeftSubList = SubList(leftList, leftList.size - 1)
            val rightSubList = SubList(rightList, rightList.size)
            val actual = sut.matches(endedLeftSubList, rightSubList, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(false, 2..2, 3..2)))
            actual shouldBe expected
        }

        "completeMatch" {
            val left = SubList(leftList)
            val actual = sut.matches(left, left, matcher = Any::equals)
            val expected = listOf(mutableListOf(RangeMatch(true, 0..2, 0..2)))
            actual shouldBe expected
        }

        "mismatch" {
            val left = SubList(leftList)
            val right = SubList(rightList)
            val actual = sut.matches(left, right, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(false, 0..0, -1..-2),
                    RangeMatch(true, 1..2, 0..1),
                    RangeMatch(false, 3..2, 2..2)
                )
            )
            actual shouldBe expected
        }

        "twoShortLists" {
            val left = SubList(listOf("A", "C"))
            val right = SubList(listOf("B", "C"))
            val actual = sut.matches(left, right, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(false, 0..0, 0..0),
                    RangeMatch(true, 1..1, 1..1)
                )
            )
            actual shouldBe expected
        }

        "twoLongerLists" {
            val left = SubList(listOf("A", "B", "E"))
            val right = SubList(listOf("C", "D", "E"))
            val actual = sut.matches(left, right, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(false, 0..1, 0..1),
                    RangeMatch(true, 2..2, 2..2)
                )
            )
            actual shouldBe expected
        }

        "mismatchInTheMiddle" {
            val left = SubList(listOf("A", "B", "E"))
            val right = SubList(listOf("A", "D", "E"))
            val actual = sut.matches(left, right, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(true, 0..0, 0..0),
                    RangeMatch(false, 1..1, 1..1),
                    RangeMatch(true, 2..2, 2..2)
                )
            )
            actual shouldBe expected
        }

        "twoEvenLongerLists" {
            val left = SubList(listOf("A", "B", "E", "E"))
            val right = SubList(listOf("A", "C", "D", "E", "E"))
            val actual = sut.matches(left, right, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(true, 0..0, 0..0),
                    RangeMatch(false, 1..1, 1..2),
                    RangeMatch(true, 2..3, 3..4)
                )
            )
            actual shouldBe expected
        }

        "twoEvenLongerListsReversed" {
            val left = SubList(listOf("A", "B", "E", "E"))
            val right = SubList(listOf("A", "C", "D", "E", "E"))
            val actual = sut.matches(right, left, matcher = Any::equals)
            val expected = listOf(
                listOf(
                    RangeMatch(true, 0..0, 0..0),
                    RangeMatch(false, 1..2, 1..1),
                    RangeMatch(true, 3..4, 2..3)
                )
            )
            actual shouldBe expected
        }

        "timesOut" {
            val longList = (1..100).toList()
                .map { "Item $it" }
            val anotherLongList = longList.drop(1).map { if (it == "Item 100") it else it.uppercase() }
            withClue("Guardian assumption") { longList[99] shouldBe anotherLongList[98] }
            val sut = ListMatcher(timeoutInMs = 1)
            val actual = sut.match(longList, anotherLongList)
            val expected = listOf(RangeMatch(false, 0..99, 0..98))
            actual shouldBe expected
        }

        "doesNotTimeOut" {
            val longList = (1..100).toList()
                .map { "Item $it" }
            val anotherLongList = longList.drop(1).map { if (it == "Item 100") it else it.uppercase() }
            withClue("Guardian assumption") { longList[99] shouldBe anotherLongList[98] }
            val sut = ListMatcher(timeoutInMs = 1000000)
            val startedAt = Instant.now()
            val actual = sut.match(longList, anotherLongList)
            println("\ndoesNotTimeOut tst ran in: ${ChronoUnit.MILLIS.between(startedAt, Instant.now())} ms")
            val expected = listOf(
                RangeMatch(false, 0..98, 0..97),
                RangeMatch(true, 99..99, 98..98)
            )
            actual shouldBe expected
        }

        "matchLists_usesMessage" {
            val expected = listOf("Yoda", "R2D2")
            val actual = listOf("R2D2", "Yoda")
            val message = "Favorite Star Wars characters"
            val thrown = shouldThrow<AssertionError> {
                matchLists(expected, actual, message = message)
            }
            thrown.message shouldBe """$message

Mismatch:
expected[0] = Yoda

Match:
expected[1] == actual[0]: R2D2

Mismatch:
actual[1] = Yoda

Possible matches:
actual[1] == expected[0], is: Yoda"""
        }
    }
}
