package com.tgt.trans.common.testhelpers.collections

import java.lang.Math.abs
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MapMatcherTest: StringSpec() {

    private val doNothingDay = mapOf<Int, String>()

    private val saturday = mapOf(1 to "Watch Movies")

    private val sunday = mapOf(1 to "Watch Movies", 2 to "Just Relax")

    init {
    "matchesEmptyMaps" {
        matchMaps(doNothingDay, doNothingDay.copy())
    }

    "matchesNonEmptyMaps" {
        matchMaps(saturday, saturday.copy())
    }

    "describesExtraKeyInActualWhenExpectedEmpty" {
        val thrown = shouldThrow<AssertionError> {
            matchMaps(doNothingDay, saturday)
        }
        thrown.message shouldBe """Key in actual, but not in expected:
key=1, value=Watch Movies"""
    }

    "describesExtraKeyInActualWhenExpectedNotEmpty" {
        val thrown = shouldThrow<AssertionError> {
            matchMaps(saturday, sunday)
        }
        thrown.message shouldBe """Key in actual, but not in expected:
key=2, value=Just Relax"""
    }

    "describesExtraKeyInExpectedWhenActualEmpty" {
        val thrown = shouldThrow<AssertionError> {
            matchMaps(saturday, doNothingDay)
        }
        thrown.message shouldBe """Key in expected, but not in actual:
key=1, value=Watch Movies"""
    }

    "describesExtraKeyInExpectedWhenActualNotEmpty" {
        val thrown = shouldThrow<AssertionError> {
            matchMaps(sunday, saturday)
        }
        thrown.message shouldBe """Key in expected, but not in actual:
key=2, value=Just Relax"""
    }

    "describesValueMismatchForSameKey" {
        val sunday = mapOf(1 to "Watch Youtube")
        val thrown = shouldThrow<AssertionError> {
            matchMaps(saturday, sunday)
        }
        thrown.message shouldBe """Values mismatch:
key=1
  expected=Watch Movies
  actual  =Watch Youtube"""
    }

    "usesCustomMessage" {
        val sunday = mapOf(1 to "Watch Youtube")
        val thrown = shouldThrow<AssertionError> {
            matchMaps(saturday, sunday, message = "Activities:")
        }
        thrown.message shouldBe """Activities:

Values mismatch:
key=1
  expected=Watch Movies
  actual  =Watch Youtube"""
    }

    "completeExample" {
        val morning1 = mutableMapOf(1 to "Wash Dishes", 2 to "Cook Breakfast", 3 to "Eat")
        val morning2 = mutableMapOf(2 to "Cook Breakfast", 3 to "Eat Slowly", 4 to "Wash Dishes")
        val thrown = shouldThrow<AssertionError> {
            matchMaps(morning1, morning2)
        }
        thrown.message shouldBe """Key in expected, but not in actual:
key=1, value=Wash Dishes

Key in actual, but not in expected:
key=4, value=Wash Dishes

Values mismatch:
key=3
  expected=Eat
  actual  =Eat Slowly"""
    }

    "describeMismatchedMaps" {
        val expectedMap = mapOf<Any, Any>("Red" to "Planet", "Yellow" to "Submarine", "White" to 42)
        val actualMap = mapOf<Any, Any>("Red" to 42, "Yellow" to "Submarine", "White" to "Snow")
        val actual = describeMismatchedMaps(expectedMap, actualMap)
        actual shouldBe """Values mismatch:
key=Red
  expected=Planet
  actual  =42
key=White
  expected=42
  actual  =Snow"""
    }

    "describeNestedMaps" {
        val expectedMap = mapOf<Any, Any>("Manager" to "Yoda", "Base" to "Tattoine",
                "Crew" to mapOf("Pilot" to "Han Solo", "Business Analyst" to "R2D2"))
        val actualMap = mapOf<Any, Any>("Manager" to "Yoda", "Base" to "Alderaan",
                "Crew" to mapOf("Pilot" to "Han Solo", "Fighter" to "Leia"))
        val actual = compareMaps(expectedMap, actualMap)
        actual.message shouldBe """Values mismatch:
key=Base
  expected=Tattoine
  actual  =Alderaan
key=Crew
  Key in expected, but not in actual:
  key=Business Analyst, value=R2D2

  Key in actual, but not in expected:
  key=Fighter, value=Leia"""
    }

    "describeTwiceNestedMaps" {
        val expectedMap = mapOf<Any, Any>("Manager" to "Yoda", "Base" to "Tattoine",
                "Crew" to mapOf<Any, Any>("Pilot" to "Han Solo",
                        "Business Analyst" to "R2D2",
                        "Interns" to mapOf("Accounting" to "Obi-wan Kenobi")))
        val actualMap = mapOf<Any, Any>("Manager" to "Yoda", "Base" to "Alderaan",
                "Crew" to mapOf<Any, Any>("Pilot" to "Han Solo",
                        "Fighter" to "Leia",
                        "Interns" to mapOf("Accounting" to "R2D2", "HR" to "C-3PO")))
        val actual = compareMaps(expectedMap, actualMap)
        actual.message shouldBe """Values mismatch:
key=Base
  expected=Tattoine
  actual  =Alderaan
key=Crew
  Key in expected, but not in actual:
  key=Business Analyst, value=R2D2

  Key in actual, but not in expected:
  key=Fighter, value=Leia

  Values mismatch:
  key=Interns
    Key in actual, but not in expected:
    key=HR, value=C-3PO

    Values mismatch:
    key=Accounting
      expected=Obi-wan Kenobi
      actual  =R2D2"""
    }

    "describeMapOfLists" {
        val expectedMap = mapOf<Any, Any>("Planets" to listOf("Tattoine", "Alderaan", "Dagobah"),
                "Crew" to listOf("Han Solo", "R2D2"))
        val actualMap = mapOf<Any, Any>("Planets" to listOf("Hoth", "Tattoine", "Alderaan"),
                "Crew" to listOf("Han Solo", "Chewbacca"))
        val actual = compareMaps(expectedMap, actualMap)
        actual.message shouldBe """Values mismatch:
key=Planets

  Mismatch:
  actual[0] = Hoth

  Match:
  expected[0] == actual[1]: Tattoine
  expected[1] == actual[2]: Alderaan

  Mismatch:
  expected[2] = Dagobah

key=Crew

  Match:
  expected[0] == actual[0]: Han Solo

  Mismatch:
  expected[1] = R2D2
  actual[1] = Chewbacca
"""
    }

    "describeMapOfSets" {
        val expectedMap = mapOf<Any, Any>("Planets" to setOf("Tattoine", "Alderaan", "Dagobah"),
                "Crew" to setOf("Han Solo", "R2D2"))
        val actualMap = mapOf<Any, Any>("Planets" to setOf("Hoth", "Tattoine", "Alderaan"),
                "Crew" to setOf("Han Solo", "Chewbacca"))
        val actual = compareMaps(expectedMap, actualMap)
        actual.message shouldBe """Values mismatch:
key=Planets
  Expected, but not in actual:
  Dagobah

  Actual, but not in expected:
  Hoth
key=Crew
  Expected, but not in actual:
  R2D2

  Actual, but not in expected:
  Chewbacca"""
    }

    "usesCustomValuesMatcher" {
        val expectedMap = mapOf(
                "Morning" to 33.1,
                "Noon" to 61.2,
                "Sunset" to 41.0)
        val actualMap = mapOf(
                "Morning" to 32.9,
                "Noon" to 61.4,
                "Sunset" to 40.6)
        withClue("Guardian Assumption: Maps not identical") {
            shouldThrow<AssertionError> {
                matchMaps(expectedMap, actualMap)
            }
        }
        val valuesMatcher: (Any, Any) -> Boolean = { expected, actual -> expected is Double
                && actual is Double
                && abs(expected - actual) < 0.5 }
        matchMaps(expectedMap, actualMap, valuesMatcher = valuesMatcher)
    }
    }
}
