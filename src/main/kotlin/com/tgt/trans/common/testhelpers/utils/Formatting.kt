package com.tgt.trans.common.testhelpers.utils

fun Iterable<String>.joinNotEmptyToString(separator: CharSequence): String {
    return this.filter{it.isNotEmpty()}.joinToString(separator)
}
