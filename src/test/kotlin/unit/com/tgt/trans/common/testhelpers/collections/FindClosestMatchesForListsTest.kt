package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.distance.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

class FindClosestMatchesForListsTest: StringSpec() {
    init {
        "find exact match in another place" {
            val expected = listOf(redCircle, blueCircle, redTriangle)
            val actual = listOf(blueCircle, redTriangle, redCircle)
            val elementMatches = listOf(
                RangeMatch(
                    match = false,
                    leftRange = -1..-2,
                    rightRange = 0..0
                ),
                RangeMatch(
                    match = true,
                    leftRange = 1..2,
                    rightRange = 0..1
                ),
                RangeMatch(
                    match = false,
                    leftRange = 2..2,
                    rightRange = -1..-2
                ),
            )

            findClosestMatchesForLists(
                expected, actual, elementMatches
            ) shouldBe listOf(
                PossibleMatch(
                    actual = IndexedElement(
                        index = 2,
                        element = Thing(color = "red", shape = "circle")
                    ),
                    matchInExpected = IndexedElement(
                        index = 0,
                        element = Thing(color = "red", shape = "circle")
                    ),
                    comparisonResult= Match(
                        field="",
                        value=Thing(color = "red", shape = "circle")
                    )
                )
            )
        }

        "find imperfect match in another place" {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenApple, tartRedCherry, sweetGreenPear)
            val elementMatches = listOf(
                RangeMatch(
                    match = true,
                    leftRange = 0..0,
                    rightRange = 0..0
                ),
                RangeMatch(
                    match = false,
                    leftRange = 1..1,
                    rightRange = 1..1
                ),
                RangeMatch(
                    match = true,
                    leftRange = 2..2,
                    rightRange = 2..2
                ),
            )

            val matchList = findClosestMatchesForLists(
                expected, actual, elementMatches
            )

            matchList shouldBe listOf(
                PossibleMatch(
                    actual = IndexedElement(
                        index = 1,
                        element = tartRedCherry
                    ),
                    matchInExpected = IndexedElement(
                        index = 1,
                        element = sweetRedApple
                    ),
                    comparisonResult= MismatchByField(
                        field="",
                        expected = sweetRedApple ,
                        actual = tartRedCherry,
                        comparisonResults = listOf(
                            AtomicMismatch("name", "apple", "cherry"),
                            Match("color", "red"),
                            AtomicMismatch("taste", "sweet", "tart"),
                        ),
                        distance = Distance(BigDecimal("0.33"))
                    )
                )
            )
        }

        "find nothing if no partial matches" {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenApple, sourYellowLemon, sweetGreenPear)
            val elementMatches = listOf(
                RangeMatch(
                    match = true,
                    leftRange = 0..0,
                    rightRange = 0..0
                ),
                RangeMatch(
                    match = false,
                    leftRange = 1..1,
                    rightRange = 1..1
                ),
                RangeMatch(
                    match = true,
                    leftRange = 2..2,
                    rightRange = 2..2
                ),
            )

            findClosestMatchesForLists(
                expected, actual, elementMatches
            ) shouldBe listOf()
        }
    }
}