package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ItemsMatchTest: StringSpec() {
    init {
        "cannotCreateMatchWithoutLeftItemPresent" {
            shouldThrow<IllegalArgumentException> {
                ItemsMatch(true, MatchType.RIGHT)
            }
        }

        "cannotCreateMatchWithoutRightItemPresent" {
            shouldThrow<IllegalArgumentException> {
                ItemsMatch(true, MatchType.LEFT)
            }
        }
    }
}