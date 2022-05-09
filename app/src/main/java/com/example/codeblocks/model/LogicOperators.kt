package com.example.codeblocks.model

enum class LogicOperators(val operator: String, val priority: Int) {
    NEGATE("~",3), OR("|",1), AND("&",2),
    OPEN_BRACKET("(",-1), CLOSED_BRACKET(")", -1),
    NOT_AN_OPERATOR("",-1)
}

fun getLogicOperator(_op: Char) = when(_op) {
        '|' -> LogicOperators.OR
        '&' -> LogicOperators.AND
        '(' -> LogicOperators.OPEN_BRACKET
        ')' -> LogicOperators.CLOSED_BRACKET
        '~' -> LogicOperators.NEGATE
        else -> LogicOperators.NOT_AN_OPERATOR
}