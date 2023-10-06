package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.temporal.ChronoUnit

class FizzBuzzListsTest: StringSpec() {
    val size = 30

    init {
        "whenBothSublistsOver".config(enabled = false) {
            val startedAt = Instant.now()
            val leftList = fizzList(size)
            println("\nFizz (left list:\n")
            leftList.forEach { println(it) }
            val endedLeftSubList = SubList(leftList, 0)
            val rightList = buzzList(size)
            println("\nBuzz (right list:\n")
            rightList.forEach { println(it) }
            println("\n===============================\n")
            val endedRightSubList = SubList(rightList, 0)
            val actual = ListMatcher().matches(endedLeftSubList, endedRightSubList, matcher = Any::equals)
            val bestMatch = bestMatch(actual)
            printMatches(leftList, rightList, bestMatch)
            val elapsed = ChronoUnit.MILLIS.between(startedAt, Instant.now())
            println("\nElapsed: $elapsed ms")
        }
    }
}

fun fizzList(to: Int): List<String>{
    val ret = mutableListOf<String>()
    for(i in 0..to){
        if(i%5 == 0){
            ret.add("Fizz $i 1")
            ret.add("Fizz $i 2")
        } else {
            ret.add("Item $i")
        }
    }
    return ret
}

fun buzzList(to: Int): List<String>{
    val ret = mutableListOf<String>()
    for(i in 0..to){
        if(i%7 == 0){
            ret.add("Buzz $i 1")
            ret.add("Buzz $i 2")
            ret.add("Buzz $i 3")
        } else {
            ret.add("Item $i")
        }
    }
    return ret
}