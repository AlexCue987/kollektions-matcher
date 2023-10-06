package com.tgt.trans.common.testhelpers.collections

fun ClosedRange<Int>.isNotEmpty() = this.start <= this.endInclusive

fun ClosedRange<Int>.extendLeft(): ClosedRange<Int> = this.start-1 .. this.endInclusive

fun ClosedRange<Int>.spawnRangeOnLeft(empty: Boolean): ClosedRange<Int> =
         (if(empty) this.start until this.start else this.start-1 until this.start)

fun ClosedRange<Int>.length() = this.endInclusive - this.start + 1

