package com.tgt.trans.common.testhelpers.collections

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GroupAndCountTest: StringSpec() {
    init {
        "groups and counts" {
            groupAndCount(listOf("apple", "orange", "orange")) shouldBe mapOf("apple" to 1, "orange" to 2)
        }
    }
}