package com.tgt.trans.common.testhelpers.collections

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class RangeMatchTest: AnnotationSpec() {
    @Test
    fun extendLeftRangeBack(){
        val sut = RangeMatch(false, 1..2, 2..3)
        val actual = sut.extendLeftRangeBack()
        val expected = RangeMatch(false, 0..2, 2..3)
        actual shouldBe expected
    }

    @Test
    fun extendRightRangeBack(){
        val sut = RangeMatch(false, 1..2, 2..3)
        val actual = sut.extendRightRangeBack()
        val expected = RangeMatch(false, 1..2, 1..3)
        actual shouldBe expected
    }

    @Test
    fun extendBothRangesBack(){
        val sut = RangeMatch(true, 1..2, 2..3)
        val actual = sut.extendBothRangesBack()
        val expected = RangeMatch(true, 0..2, 1..3)
        actual shouldBe expected
    }

    @Test
    fun spawnNewRange_mismatchLEFT(){
        val sut = RangeMatch(true, 1..2, 2..3)
        val actual = sut.spawnNewRange(ItemsMatch(false, MatchType.LEFT))
        val expected = RangeMatch(false, 0..0, 1..0)
        actual shouldBe expected
    }

    @Test
    fun spawnNewRange_mismatchRIGHT(){
        val sut = RangeMatch(true, 1..2, 2..3)
        val actual = sut.spawnNewRange(ItemsMatch(false, MatchType.RIGHT))
        val expected = RangeMatch(false, 0..-1, 1..1)
        actual shouldBe expected
    }

    @Test
    fun spawnNewRange_mismatchBOTH(){
        val sut = RangeMatch(true, 1..2, 2..3)
        val actual = sut.spawnNewRange(ItemsMatch(false, MatchType.BOTH))
        val expected = RangeMatch(false, 0..0, 1..1)
        actual shouldBe expected
    }

    @Test
    fun spawnNewRange_matchBOTH(){
        val sut = RangeMatch(false, 1..2, 2..3)
        val actual = sut.spawnNewRange(ItemsMatch(true, MatchType.BOTH))
        val expected = RangeMatch(true, 0..0, 1..1)
        actual shouldBe expected
    }

    @Test
    fun rightIndexes_WorksForSeveralIndexes() {
        val sut = RangeMatch(false, 1..2, 2..4)
        sut.rightIndexes shouldBe listOf(2, 3, 4)
    }

    @Test
    fun rightIndexes_WorksForOneIndex() {
        val sut = RangeMatch(false, 1..2, 2..2)
        sut.rightIndexes shouldBe listOf(2)
    }

    @Test
    fun init_ifMatchRangesHaveSameLength(){
        shouldThrow<IllegalArgumentException> {
            RangeMatch(true, 1..2, 3..5)
        }
    }

    @Test
    fun init_ifLeftRangeHasIncorrectLength(){
        shouldThrow<IllegalArgumentException> {
            RangeMatch(false, 1..-1, 3..5)
        }
    }

    @Test
    fun init_ifRightRangeHasIncorrectLength(){
        shouldThrow<IllegalArgumentException> {
            RangeMatch(false, 1..2, 3..1)
        }
    }
}