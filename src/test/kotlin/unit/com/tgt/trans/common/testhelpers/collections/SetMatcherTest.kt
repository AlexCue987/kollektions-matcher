package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import io.kotest.core.spec.style.StringSpec

class SetMatcherTest: StringSpec(){
    val dogs = setOf("Dachshund", "Beagle", "Australian Cattle Dog", "German Shepherd")
    val pets = setOf("Dachshund", "Beagle", "Cat", "Parrot")

    init {
        
        "mismatches" {
            val message = """Expected, but not in actual:
Australian Cattle Dog
German Shepherd

Actual, but not in expected:
Cat
Parrot"""
            val thrown = shouldThrow<AssertionError> {
                matchSets(dogs, pets)
            }
            thrown.message shouldBe message
        }

        
        "mismatchesWithDescription" {
            val message = """Dogs:
Expected, but not in actual:
Australian Cattle Dog
German Shepherd

Actual, but not in expected:
Cat
Parrot"""
            val thrown = shouldThrow<AssertionError> {
                matchSets(dogs, pets, message = "Dogs")
            }
            thrown.message shouldBe message
        }

        
        "matches" {
            matchSets(pets, pets)
        }

        
        "expectedNotActualDetected" {
            val someDogs = setOf("Dachshund", "Beagle", "Australian Cattle Dog")
            val message = """Expected, but not in actual:
German Shepherd"""
            val thrown = shouldThrow<AssertionError> {
                matchSets(dogs, someDogs)
            }
            thrown.message shouldBe message
        }

        
        "actualNotExpectedDetected" {
            val someDogs = setOf("Dachshund", "Beagle", "Australian Cattle Dog")
            val message = """Actual, but not in expected:
German Shepherd"""
            val thrown = shouldThrow<AssertionError> {
                matchSets(someDogs, dogs)
            }
            thrown.message shouldBe message
        }

        "include possible matches" {
            val thrown = shouldThrow<AssertionError> {
                matchSets(
                    setOf(sweetGreenApple, bitterPurplePlum),
                    setOf(sweetRedApple, bitterPurplePlum)
                )
            }
            thrown.message shouldBe """Expected, but not in actual:
Fruit(name=apple, color=green, taste=sweet)

Actual, but not in expected:
Fruit(name=apple, color=red, taste=sweet)

Possible Matches:
 expected: Fruit(name=apple, color=green, taste=sweet), 
  but was: Fruit(name=apple, color=red, taste=sweet), 
  distance: 0.67,
  fields:
  name = apple
  color expected: green, but was: red
  taste = sweet"""
        }
    }
}