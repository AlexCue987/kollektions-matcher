package com.tgt.trans.common.testhelpers.examples.part1.comparing.collections

import com.tgt.trans.common.testhelpers.collections.matchSets
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Step1_Sets: StringSpec() {
    private val fleet1 = setOf(millenniumFalcon, ghost, blockadeRunner1, blockadeRunner2)
    private val fleet2 = setOf(millenniumFalcon, ghost, blockadeRunner1, blockadeRunner3)

    init {
        "Is supposed to fail with one long error message".config(enabled = false) {
            fleet1 shouldBe fleet2
        }

        "Is supposed to fail, but with more readable message".config(enabled = false) {
            matchSets(fleet1, fleet2)
        }

        "example From ReadmeMd".config(enabled = false) {
            matchSets(
                setOf("Red", "Blue", "White"),
                setOf("Red", "Blue", "Amber")
            )
        }
    }
}