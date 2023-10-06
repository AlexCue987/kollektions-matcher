package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.text.lowercase

class ListWithCustomMatcherTest: StringSpec() {
    val crew = listOf("Yoda", "Chewbacca", "Luke")
    val sameCrewInLowerCase = crew.map { it.lowercase() }

    init {
        "matchesStringsWithCustomMatcher" {
            shouldThrow<AssertionError> {
                matchLists(crew, sameCrewInLowerCase)
            }
            matchLists(crew, sameCrewInLowerCase, { a: String, b: String -> a.lowercase() == b.lowercase() })
        }

        "findsMismatchesInStringsWithCustomMatcher" {
            val anotherCrew = listOf("Yoda", "Chewbacca", "Leia")
            val thrown = shouldThrow<AssertionError> {
                matchLists(crew, anotherCrew, { a: String, b: String -> a.lowercase() == b.lowercase() })
            }
            thrown.message shouldBe """
Match:
expected[0] == actual[0]: Yoda
expected[1] == actual[1]: Chewbacca

Mismatch:
expected[2] = Luke
actual[2] = Leia
"""
        }

        "matchesDoublesWithTolerance" {
            val expected = listOf(1.0, 2.0)
            val actual = listOf(0.91, 2.09)
            shouldThrow<AssertionError> {
                matchLists(expected, actual)
            }
            matchLists(expected, actual, { a, b -> b in (a - 0.1)..(a + 0.1) })
        }

        @Test
        fun findsMismatchesInDoublesWithTolerance() {
            val expected = listOf(1.0, 2.0)
            val actual = listOf(0.949, 2.51)
            val thrown = shouldThrow<AssertionError> {
                matchLists(expected, actual, { a, b -> b in (a - 0.05)..(a + 0.05) })
            }
            thrown.message shouldBe """
Mismatch:
expected[0] = 1.0
expected[1] = 2.0
actual[0] = 0.949
actual[1] = 2.51
"""
        }
    }
}