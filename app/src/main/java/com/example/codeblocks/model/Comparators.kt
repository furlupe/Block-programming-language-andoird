package com.example.codeblocks.model

enum class Comparators() {
    LESS, GREATER,
    EQUAL, NOT_EQUAL,
    LESS_OR_EQUAL, GREATER_OR_EQUAL
}

fun getComparator(sign: String): Comparators =
    when (sign) {
        "<" -> Comparators.LESS
        ">" -> Comparators.GREATER
        "=" -> Comparators.EQUAL
        "!=" -> Comparators.NOT_EQUAL
        "<=" -> Comparators.LESS_OR_EQUAL
        ">=" -> Comparators.GREATER_OR_EQUAL
        else -> throw Exception("$sign is not a comparator")
    }