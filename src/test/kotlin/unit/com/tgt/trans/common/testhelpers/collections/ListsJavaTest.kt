package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.TestList
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ListsJavaTest: StringSpec() {
    val testLists = TestList()

    init {
        "matches" {
            val list1 = testLists.get1()
            val list2 = testLists.get1()
            matchLists(list1, list2)
        }

        "findsMismatch" {
            val list1 = testLists.get1()
            val list2 = testLists.get2()
            val thrown = shouldThrow<AssertionError> {
                matchLists(list1, list2)
            }
            thrown.message shouldBe """
Match:
expected[0] == actual[0]: 0

Mismatch:
expected[1] = 1
actual[1] = 10
"""
        }
    }
}