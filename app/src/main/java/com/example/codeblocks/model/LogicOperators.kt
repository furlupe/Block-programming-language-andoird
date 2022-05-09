package com.example.codeblocks.model

enum class LogicOperators(val operator: String, val priority: Int) {
    NEGATE("~",3),
    AND("&",2),
    OR("|",1), XOR("^", 1),
    OPEN_BRACKET("(",-1), CLOSED_BRACKET(")", -1),
    NOT_AN_OPERATOR("",-1)
}

fun getLogicOperator(_op: Char) = when(_op) {
        '~' -> LogicOperators.NEGATE
        '&' -> LogicOperators.AND
        '|' -> LogicOperators.OR
        '^' -> LogicOperators.XOR
        '(' -> LogicOperators.OPEN_BRACKET
        ')' -> LogicOperators.CLOSED_BRACKET
        else -> LogicOperators.NOT_AN_OPERATOR
}