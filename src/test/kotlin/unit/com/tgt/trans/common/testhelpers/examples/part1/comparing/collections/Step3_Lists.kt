package com.tgt.trans.common.testhelpers.examples.part1.comparing.collections

import com.tgt.trans.common.testhelpers.collections.matchLists
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Step3_Lists: StringSpec() {
    private val fleet1 = listOf(millenniumFalcon, ghost, blockadeRunner1, blockadeRunner2)
    private val fleet2 = listOf(blockadeRunner3, millenniumFalcon, ghost, blockadeRunner1, blockadeRunner3)

    init {
        "Is supposed to fail".config(enabled = false) {
            fleet1 shouldBe fleet2
        }

        "Is supposed to fail, but with more readable message".config(enabled = false) {
            matchLists(fleet1, fleet2)
        }

        "describesMismatch" {
            val expectedMessage = """
Mismatch:
actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)

Match:
expected[0] == actual[1]: SpaceShip(name=Millennium Falcon, description=Han Solo's ship, range=10, capacity=10, serialNumber=123456)
expected[1] == actual[2]: SpaceShip(name=Ghost, description=seen in Rogue One, range=10, capacity=10, serialNumber=Unknown)
expected[2] == actual[3]: SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

Mismatch:
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)
actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)

Possible matches:
actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[2] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123456,
 but was: 123457

actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123356,
 but was: 123457

actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[2] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123456,
 but was: 123457

actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123356,
 but was: 123457"""
            val thrown = shouldThrow<AssertionError> {
                matchLists(fleet1, fleet2)
            }
            thrown.message shouldBe expectedMessage
        }

        "exampleFromReadmeMd".config(enabled = false) {
            matchLists(
                listOf("Cook", "Eat", "Wash Dishes"),
                listOf("Wash Dishes", "Cook", "Eat")
            )
        }
    }
}