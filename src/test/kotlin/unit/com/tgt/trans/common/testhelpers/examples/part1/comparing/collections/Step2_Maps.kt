package com.tgt.trans.common.testhelpers.examples.part1.comparing.collections

import com.tgt.trans.common.testhelpers.collections.matchMaps
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Step2_Maps: StringSpec() {
    private val fleet1 = mapOf(1 to millenniumFalcon, 2 to ghost, 3 to blockadeRunner1, 4 to blockadeRunner2)

    private val fleet2 = mapOf(1 to millenniumFalcon, 2 to ghost, 5 to blockadeRunner1, 4 to blockadeRunner3)

    init {
        "Is supposed to fail".config(enabled = false) {
            fleet1 shouldBe fleet2
        }

        "Is supposed to fail, but with more readable message".config(enabled = false) {
            matchMaps(fleet1, fleet2)
        }

        "matches" {
            matchMaps(fleet1, fleet1.copy())
        }

        "describesMismatch" {
            val expectedMessage = """Key in expected, but not in actual:
key=3, value=SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

Key in actual, but not in expected:
key=5, value=SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

Values mismatch:
key=4
  expected=SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)
  actual  =SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)"""
            val thrown = shouldThrow<AssertionError> {
                matchMaps(fleet1, fleet2)
            }
            thrown.message shouldBe expectedMessage
        }

        "example From ReadmeMd".config(enabled = false) {
            matchMaps(
                mapOf("Red" to "Apple", "Blue" to "Planet", "White" to "Sugar"),
                mapOf("Red" to "Apple", "Blue" to "Moon", "Amber" to "Light")
            )
        }
    }
}

fun<K, V> Map<K, V>.copy() = this.entries.asSequence().associate { it.key to it.value }
