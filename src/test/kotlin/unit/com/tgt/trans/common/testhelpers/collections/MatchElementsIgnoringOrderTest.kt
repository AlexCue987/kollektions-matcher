package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import io.kotest.core.spec.style.StringSpec

class MatchElementsIgnoringOrderTest: StringSpec() {
    init {
        "match empty collections" {
            matchElementsIgnoringOrder(listOf<Int>(), listOf<Int>())
        }

        "match non empty collections" {
            matchElementsIgnoringOrder(
                listOf("apple", "apple", "orange"),
                listOf("apple", "orange", "apple")
            )
        }
        "do not match non empty collections" {
            val thrown = shouldThrow<AssertionError>{
                matchElementsIgnoringOrder(
                    listOf("apple", "orange"),
                    listOf("apple", "orange", "apple")
                )
            }
            thrown.message shouldBe """Values mismatch:
key=apple
  expected=1
  actual  =2"""
        }

        "include possible matches" {
            val thrown = shouldThrow<AssertionError> {
                matchElementsIgnoringOrder(
                    setOf(sweetGreenApple, bitterPurplePlum),
                    setOf(sweetRedApple, bitterPurplePlum)
                )
            }
            thrown.message shouldBe """Key in expected, but not in actual:
key=Fruit(name=apple, color=green, taste=sweet), value=1

Key in actual, but not in expected:
key=Fruit(name=apple, color=red, taste=sweet), value=1

Possible Matches:
 expected: Fruit(name=apple, color=green, taste=sweet), 
  but was: Fruit(name=apple, color=red, taste=sweet), 
  distance: 0.67,
  fields:
  name = apple
  color expected: green, but was: red
  taste = sweet"""
        }

        "example".config(enabled = false) {
            matchElementsIgnoringOrder(
                listOf(sweetGreenApple, bitterPurplePlum),
                listOf(sweetRedApple, sweetGreenPear, bitterPurplePlum)
            )
        }
    }
}