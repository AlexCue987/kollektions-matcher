package com.tgt.trans.common.testhelpers.distance

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FieldsReaderTest: StringSpec() {
    private val systemToTest = FieldsReader()

    init {
        "works for data class" {
            systemToTest.fieldsOf(redCircle) shouldBe listOf(
                FieldAndValue("color", "red"),
                FieldAndValue("shape", "circle"),
            )
        }

        "works for data class with private fields" {
            systemToTest.fieldsOf(redCircleWithPrivateField) shouldBe listOf(
                FieldAndValue("color", "red"),
                FieldAndValue("shape", "circle"),
            )
        }
    }
}