package com.tgt.trans.common.testhelpers.collections

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

class SetClosestMatchesTest: StringSpec() {
    init {
        "handle both empty" {
            closestMatches<String>(
                listOf(),
                listOf()
            ) shouldBe listOf()
        }

        "handle empty actual" {
            closestMatches(
                listOf(),
                listOf("one")
            ) shouldBe listOf()
        }

        "handle empty expected" {
            closestMatches(
                listOf("one"),
                listOf()
            ) shouldBe listOf()
        }

        "no partial matches" {
            closestMatches(
                listOf(sweetGreenApple, sweetRedApple),
                listOf(sourYellowLemon, bitterPurplePlum)
            ) shouldBe listOf()
        }

        "some partial matches, no ties" {
            val actual = closestMatches(
                listOf(sweetGreenPear, sourYellowLemon),
                listOf(sweetGreenApple, bitterPurplePlum)
            )
            assertSoftly {
                actual.size shouldBe 1
                actual[0].value shouldBe sweetGreenApple
                actual[0].possibleMatch shouldBe sweetGreenPear
                actual[0].comparisonResult.distance.distance shouldBe BigDecimal("0.67")
            }
        }

        "some partial matches with ties" {
            val actual = closestMatches(
                listOf(sweetGreenPear, sweetRedApple, tartRedCherry),
            listOf(sweetGreenApple, bitterPurplePlum)
            )
            assertSoftly {
                actual.size shouldBe 2
                val sweetGreenMatch = actual.find { it.possibleMatch == sweetGreenPear}
                sweetGreenMatch!!.value shouldBe sweetGreenApple
                sweetGreenMatch.possibleMatch shouldBe sweetGreenPear
                sweetGreenMatch.comparisonResult.distance.distance shouldBe BigDecimal("0.67")
                val sweetAppleMatch = actual.find { it.possibleMatch == sweetRedApple}
                sweetAppleMatch!!.value shouldBe sweetGreenApple
                sweetAppleMatch.possibleMatch shouldBe sweetRedApple
                sweetAppleMatch.comparisonResult.distance.distance shouldBe BigDecimal("0.67")
            }
        }

        "handle collections as fields with no match" {
            closestMatches(listOf(largeToolBox), listOf(trinketsBox)).isEmpty() shouldBe true
        }

        "handle collections as fields with partial match" {
            val actual = closestMatches(listOf(mediumToolBox), listOf(largeToolBox))
            assertSoftly {
                actual.size shouldBe 1
                actual[0].comparisonResult.distance.distance shouldBe BigDecimal("0.67")
            }
        }
    }
}

data class MyBox(
    val label: String,
    val size: BoxSize,
    val items: List<String>
)

enum class BoxSize { SMALL, MEDIUM, LARGE }

val mediumToolBox = MyBox("tools", BoxSize.MEDIUM, listOf("hammer", "pliers", "hacksaw"))
val largeToolBox = MyBox("tools", BoxSize.LARGE, listOf("hammer", "pliers", "hacksaw"))
val trinketsBox = MyBox("trinkets", BoxSize.SMALL, listOf("souvenir"))
