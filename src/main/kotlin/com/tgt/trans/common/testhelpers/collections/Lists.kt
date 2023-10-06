package com.tgt.trans.common.testhelpers.collections

import com.tgt.trans.common.testhelpers.distance.ComparisonResult
import com.tgt.trans.common.testhelpers.distance.Match
import com.tgt.trans.common.testhelpers.distance.MismatchByField
import com.tgt.trans.common.testhelpers.distance.possibleMatchDescription
import com.tgt.trans.common.testhelpers.utils.joinNotEmptyToString
import java.time.Instant
import java.util.*

fun<T> matchLists(expected: List<T>,
                     actual: List<T>,
                     matcher: (left: T, right: T) -> Boolean = {left, right -> left == right},
                     timeoutInMs: Long = 100L,
                     message: String = "") {
    val assertionResult = compareLists(expected, actual, matcher, timeoutInMs, message)
    assertionResult.assertSuccess()
}

fun <T> compareLists(expected: List<T>, actual: List<T>,
                     matcher: (left: T, right: T) -> Boolean = {left, right -> left == right},
                     timeoutInMs: Long = 100L,
                     message: String = ""): AssertionResult {
    val listMatcher = ListMatcher(timeoutInMs)
    val results = listMatcher.match(expected, actual, matcher)
    return if (results.size == 1 && results[0].match) {
        AssertionResult(true)
    } else {
        val mismatchDescription = listOf(message,
                printMatches(expected, actual, results))
                .joinNotEmptyToString("\n")
        val possibleMatches = findClosestMatchesForLists(expected, actual, results)
        val possibleMatchDescription = if(possibleMatches.isEmpty()) ""
        else "\nPossible matches:\n${possibleMatches.joinToString("\n\n") { possibleMatchDescription(it) }}"
        AssertionResult(false, "$mismatchDescription$possibleMatchDescription")
    }
}


data class SubList<T>(val items: List<T>, val offset: Int = 0){
    fun next(): SubList<T>{
        return if(offset < items.size) SubList(items, offset+1)
        else this
    }

    fun isOver() = (offset == items.size)

    fun onLastItem() = (offset == (items.size-1))

    fun rangeToEnd() = offset until items.size

    fun current(): T = if(isOver()) throw IllegalStateException("IsOver, no current item") else items[offset]
}

enum class BranchDirection{ALL, LEFT, MIDDLE, RIGHT}

class ListMatcher(timeoutInMs: Long = 100){
    private val stopAt: Instant = Instant.now().plusMillis(timeoutInMs)

    fun <T> match(expected: List<T>,
                     actual: List<T>,
                     matcher: (left: T, right: T) -> Boolean = {left, right -> left == right}): List<RangeMatch> {
        return try{
            val matches = matches(SubList(expected), SubList(actual), matcher= matcher)
            bestMatch(matches)
        }catch (ex: MatchTimedOut){
            println("Match timed out.")
            listOf(listsDoNotMatch(expected, actual))
        }
    }

    private fun <T> listsDoNotMatch(expected: List<T>, actual: List<T>) =
            RangeMatch(false, 0 until expected.size, 0 until actual.size)

    fun <T> matches(expected: SubList<T>,
                                   actual: SubList<T>,
                                   branchDirection: BranchDirection = BranchDirection.ALL,
                                   matcher: (left: T, right: T) -> Boolean):
            List<List<RangeMatch>> {
        checkTime()
        return when {
            (expected.onLastItem() && actual.onLastItem()) ->
                getNewTailOffLastItems(expected, actual, matcher = matcher)

            (expected.isOver() || actual.isOver()) ->
                getMismatchedTails(expected, actual)

            matcher(expected.current(), actual.current()) ->
                matchCurrentItemsAndCompareTails(MATCH, expected, actual, matcher = matcher)

            else -> branchOnMismatch(expected, actual, branchDirection, matcher = matcher)
        }
    }

    private val branchLeftOn = listOf(BranchDirection.ALL, BranchDirection.LEFT)

    private val branchRightOn = listOf(BranchDirection.ALL, BranchDirection.RIGHT)

    private fun <T> branchOnMismatch(expected: SubList<T>, actual: SubList<T>,
                                                     branchDirection: BranchDirection,
                                                     matcher: (left: T, right: T) -> Boolean): List<List<RangeMatch>> {
        val allTails: MutableList<List<RangeMatch>> = mutableListOf()
        if(branchDirection in branchLeftOn) {
            allTails.addAll(nextOnLeftAndCompareTails(expected, actual, matcher = matcher))
        }
        allTails.addAll(matchCurrentItemsAndCompareTails(MISMATCH, expected, actual, matcher = matcher))
        if(branchDirection in branchRightOn) {
            allTails.addAll(nextOnRightAndCompareTails(expected, actual, matcher = matcher))
        }
        return bestTwoMatches(allTails)
    }

    private fun <T> matchCurrentItemsAndCompareTails(itemsMatch: ItemsMatch,
                                                                     expected: SubList<T>,
                                                                     actual: SubList<T>,
                                                                     matcher: (left: T, right: T) -> Boolean): List<List<RangeMatch>> {
        val branchType = if(itemsMatch.match) BranchDirection.ALL else BranchDirection.MIDDLE
        val matchesForTail = matches(expected.next(), actual.next(), branchType, matcher = matcher)
        return addItemsMatchToMatchesForTail(itemsMatch, matchesForTail)
    }

    private fun <T> nextOnLeftAndCompareTails(expected: SubList<T>,
                                                              actual: SubList<T>,
                                                              matcher: (left: T, right: T) -> Boolean): List<List<RangeMatch>> {
        val matchesForTail = matches(expected.next(), actual, BranchDirection.LEFT, matcher = matcher)
        return addItemsMatchToMatchesForTail(LEFT_ITEM_ONLY, matchesForTail)
    }

    private fun <T> nextOnRightAndCompareTails(expected: SubList<T>,
                                                               actual: SubList<T>,
                                                               matcher: (left: T, right: T) -> Boolean): List<List<RangeMatch>> {
        val matchesForTail = matches(expected, actual.next(), BranchDirection.RIGHT, matcher = matcher)
        return addItemsMatchToMatchesForTail(RIGHT_ITEM_ONLY, matchesForTail)
    }

    private fun <T> getMismatchedTails(expected: SubList<T>, actual: SubList<T>) =
            listOf(mutableListOf(RangeMatch(false, expected.rangeToEnd(), actual.rangeToEnd())))

    private fun <T> getNewTailOffLastItems(expected: SubList<T>, actual: SubList<T>,
                                                           matcher: (left: T, right: T) -> Boolean): List<List<RangeMatch>> {
        val matched = matcher(expected.current(), actual.current())
        return listOf(mutableListOf(RangeMatch(matched, expected.rangeToEnd(), actual.rangeToEnd())))
    }

    fun addItemsMatchToMatchesForTail(itemsMatch: ItemsMatch, matchesForTail: List<List<RangeMatch>>):
            List<List<RangeMatch>> = matchesForTail.map { addItemsMatchToOneTail(itemsMatch, it) }

    fun addItemsMatchToOneTail(itemsMatch: ItemsMatch, matchesForTail: List<RangeMatch>):
            List<RangeMatch>{
        require(matchesForTail.isNotEmpty()) { "matchesForTail cannot be empty" }
        return when{
            matchesForTail[0].match == itemsMatch.match -> {
                val extendedFirstRange = when(itemsMatch.matchType){
                    MatchType.LEFT -> matchesForTail[0].extendLeftRangeBack()
                    MatchType.RIGHT -> matchesForTail[0].extendRightRangeBack()
                    MatchType.BOTH -> matchesForTail[0].extendBothRangesBack()
                }
                listOf(extendedFirstRange, *matchesForTail.drop(1).toTypedArray())
            }
            else -> {
                val newRangeMatch = matchesForTail[0].spawnNewRange(itemsMatch)
                listOf(newRangeMatch, *matchesForTail.toTypedArray())
            }
        }
    }

    private fun checkTime(){
        if(Instant.now().isAfter(stopAt)){
            throw MatchTimedOut()
        }
    }
}

class MatchTimedOut: Exception()
