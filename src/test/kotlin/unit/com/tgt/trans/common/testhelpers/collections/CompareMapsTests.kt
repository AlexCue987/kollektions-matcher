package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MatchMapsTests: StringSpec() {
    val tShirt = mapOf("size" to "M", "kind" to "T-Shirt", "color" to "Blue", "text" to "I Love Huskies")

    init {
        "matches" {
            val actual = compareMaps(tShirt, tShirt)
            val expected = AssertionResult(true)
            actual shouldBe expected
        }

        "mismatch_expectedNotActual" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }
            val actual = compareMaps(tShirt, colorlessTShirt)
            val expected = AssertionResult(
                false, """Key in expected, but not in actual:
key=color, value=Blue"""
            )
            actual shouldBe expected
        }

        "mismatch_actualNotExpected" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }
            val actual = compareMaps(colorlessTShirt, tShirt)
            val expected = AssertionResult(
                false, """Key in actual, but not in expected:
key=color, value=Blue"""
            )
            actual shouldBe expected
        }

        "mismatch_actualAndExpectedDiffer" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }
            val textlessTShirt = tShirt.copy().filter { it.key != "text" }
            val actual = compareMaps(colorlessTShirt, textlessTShirt)
            val expected = AssertionResult(
                false, """Key in expected, but not in actual:
key=text, value=I Love Huskies

Key in actual, but not in expected:
key=color, value=Blue"""
            )
            actual shouldBe expected
        }

        "mismatch_valuesDiffer" {
            val anotherTShirt = tShirt.copy().toMutableMap()
            anotherTShirt["color"] = "Purple"
            val actual = compareMaps(tShirt, anotherTShirt)
            val expected = AssertionResult(
                false, """Values mismatch:
key=color
  expected=Blue
  actual  =Purple"""
            )
            actual shouldBe expected
        }

        "mismatch_expectedNotActual_valuesDiffer" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }.toMutableMap()
            colorlessTShirt["text"] = "I Love Dogs"
            val actual = compareMaps(tShirt, colorlessTShirt)
            val expected = AssertionResult(
                false, """Key in expected, but not in actual:
key=color, value=Blue

Values mismatch:
key=text
  expected=I Love Huskies
  actual  =I Love Dogs"""
            )
            actual shouldBe expected
        }

        "mismatch_actualNotExpected_valuesDiffer" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }.toMutableMap()
            colorlessTShirt["text"] = "I Love Dogs"
            val actual = compareMaps(colorlessTShirt, tShirt)
            val expected = AssertionResult(
                false, """Key in actual, but not in expected:
key=color, value=Blue

Values mismatch:
key=text
  expected=I Love Dogs
  actual  =I Love Huskies"""
            )
            actual shouldBe expected
        }

        "mismatch_actualAndExpectedDiffer_valuesDiffer" {
            val colorlessTShirt = tShirt.copy().filter { it.key != "color" }.toMutableMap()
            colorlessTShirt["size"] = "S"
            colorlessTShirt["kind"] = "A-Shirt"
            val textlessTShirt = tShirt.copy().filter { it.key != "text" }
            val actual = compareMaps(colorlessTShirt, textlessTShirt)
            val expected = AssertionResult(
                false, """Key in expected, but not in actual:
key=text, value=I Love Huskies

Key in actual, but not in expected:
key=color, value=Blue

Values mismatch:
key=size
  expected=S
  actual  =M
key=kind
  expected=A-Shirt
  actual  =T-Shirt"""
            )
            actual shouldBe expected
        }

        "supportNullValues" {
            val map1 = mapOf("a" to 1, "b" to null, "c" to null)
            val map2 = mapOf("a" to null, "b" to 2, "c" to null)
            val actual = compareMaps(map1, map2)
            actual.message shouldBe
                """Values mismatch:
key=a
  expected=1
  actual  =null
key=b
  expected=null
  actual  =2"""
        }
    }
}

fun<K, V> Map<K, V>.copy(): Map<K, V> = this.asSequence().associate { it.key to it.value }

