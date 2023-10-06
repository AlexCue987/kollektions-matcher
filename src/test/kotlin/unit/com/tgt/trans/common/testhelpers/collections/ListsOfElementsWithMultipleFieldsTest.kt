package com.tgt.trans.common.testhelpers.collections

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ListsOfElementsWithMultipleFieldsTest: StringSpec() {
    // these tests are demos
    init {
        "find exact match".config(enabled = false) {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenPear, sweetGreenApple, sweetRedApple)
            matchLists(expected, actual)
        }

        "find partial matches".config(enabled = false) {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenPear.copy(name = "onion"), sweetGreenApple, sweetRedApple)
            matchLists(expected, actual)
        }

        "find partial match, one element".config(enabled = false) {
            val expected = listOf(sweetGreenApple)
            val actual = listOf(sweetRedApple)
            matchLists(expected, actual)
        }

        "find no matches".config(enabled = false) {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sourYellowLemon, sweetGreenApple, sweetRedApple)
            matchLists(expected, actual)
        }

        "find exact match old style".config(enabled = false) {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenPear, sweetGreenApple, sweetRedApple)
            actual shouldBe expected
        }

        "find partial match old style".config(enabled = false) {
            val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
            val actual = listOf(sweetGreenPear.copy(name = "onion"), sweetGreenApple, sweetRedApple)
            actual shouldBe expected
        }
    }

    data class Fruit(
        val name: String,
        val color: String,
        val taste: String
    )

    val sweetGreenApple = Fruit("apple", "green", "sweet")
    val sweetRedApple = Fruit("apple", "red", "sweet")
    val sweetGreenPear = Fruit("pear", "green", "sweet")
    val sourYellowLemon = Fruit("lemon", "yellow", "sour")
    val tartRedCherry = Fruit("cherry", "red", "tart")

}