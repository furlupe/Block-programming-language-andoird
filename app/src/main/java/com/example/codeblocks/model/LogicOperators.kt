package com.example.codeblocks.model

enum class LogicOperators(val priority: Int) {
    NEGATE(3), OR(1), AND(2), EQUAL_LOGIC(0), NOT_AN_OPERATOR(-1),
    OPEN_BRACKET(-1), CLOSED_BRACKET(-1)
}

fun getLogicOperator(_op: Char) = when(_op) {
        '|' -> LogicOperators.OR
        '&' -> LogicOperators.AND
        '~' -> LogicOperators.EQUAL_LOGIC
        '(' -> LogicOperators.OPEN_BRACKET
        ')' -> LogicOperators.CLOSED_BRACKET
        '_' -> LogicOperators.NEGATE
        else -> LogicOperators.NOT_AN_OPERATOR
}
