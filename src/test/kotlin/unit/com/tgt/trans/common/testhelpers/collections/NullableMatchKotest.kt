package com.tgt.trans.common.testhelpers.collections

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NullableMatchKotest: StringSpec() {
    init {
        "true if both null" {
            nullableMatch(null, null) shouldBe true
        }

        "false if only one null" {
            nullableMatch(1, null) shouldBe false
            nullableMatch(null, 1) shouldBe false
        }

        "both not null" {
            nullableMatch(1, 1) shouldBe true
            nullableMatch(0, 1) shouldBe false
        }
    }
}