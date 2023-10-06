package com.tgt.trans.common.testhelpers.collections

data class RangeMatch(val match: Boolean, val leftRange: ClosedRange<Int>, val rightRange: ClosedRange<Int>){
    init{
        require(!match || (leftRange.length() == rightRange.length())){
            "For a match, both ranges must have same length: $this"
        }
        require(leftRange.length() >= 0){
            "Invalid left range: $this"
        }
        require(rightRange.length() >= 0){
            "Invalid right range: $this"
        }
    }

    fun spawnNewRange(itemsMatch: ItemsMatch) =
            RangeMatch(itemsMatch.match,
                    leftRange.spawnRangeOnLeft(!itemsMatch.leftItemPresent()),
                    rightRange.spawnRangeOnLeft(!itemsMatch.rightItemPresent())
            )

    fun extendLeftRangeBack() = RangeMatch(match, extendIndexRangeBack(leftRange), rightRange)

    fun extendRightRangeBack() = RangeMatch(match, leftRange, extendIndexRangeBack(rightRange))

    fun extendBothRangesBack() = RangeMatch(match, extendIndexRangeBack(leftRange), extendIndexRangeBack(rightRange))

    private fun extendIndexRangeBack(range: ClosedRange<Int>) = range.start - 1 .. range.endInclusive

    val leftIndexes: List<Int>
        get() = leftRange.toSequence().toList()

    val rightIndexes: List<Int>
        get() = rightRange.toSequence().toList()
}
//
//fun rangeToSequence(range: ClosedRange<Int>): Sequence<Int> = sequence {
//    var next = range.start
//    while(next <= range.endInclusive) {
//        yield(next++)
//    }
//}

fun ClosedRange<Int>.toSequence(): Sequence<Int> {
    var next = this.start
    val end = this.endInclusive
    return sequence {
        while (next <= end) {
            yield(next++)
        }
    }
}

enum class MatchType(val leftItemPresent: Boolean, val rightItemPresent: Boolean){
    LEFT(true, false),
    RIGHT(false, true),
    BOTH(true, true)
}

data class ItemsMatch(val match: Boolean, val matchType: MatchType) {
    init {
        require (!match || (matchType == MatchType.BOTH)) {
            "Both sides must be present for a match: $this"
        }
    }

    fun leftItemPresent() = matchType.leftItemPresent

    fun rightItemPresent() = matchType.rightItemPresent
}

val MATCH = ItemsMatch(true, MatchType.BOTH)
val MISMATCH = ItemsMatch(false, MatchType.BOTH)
val LEFT_ITEM_ONLY = ItemsMatch(false, MatchType.LEFT)
val RIGHT_ITEM_ONLY = ItemsMatch(false, MatchType.RIGHT)