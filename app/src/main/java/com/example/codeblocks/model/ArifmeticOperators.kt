package com.example.codeblocks.model

enum class ArifmeticOperators(val operator: String, val priority: Int) {
    OPEN_BRACKET("(", 0), CLOSED_BRACKET(")", 0),
    PLUS("+", 1), MINUS("-", 1),
    FRACTION("/", 3), MULTIPLY("*", 3), MOD("%", 3),
    NOT_AN_OPERATION("", -1);
}

fun getArifmeticOperator(s: String): ArifmeticOperators =
    when (s) {
        "(" -> ArifmeticOperators.OPEN_BRACKET
        ")" -> ArifmeticOperators.CLOSED_BRACKET
        "+" -> ArifmeticOperators.PLUS
        "-" -> ArifmeticOperators.MINUS
        "/" -> ArifmeticOperators.FRACTION
        "*" -> ArifmeticOperators.MULTIPLY
        "%" -> ArifmeticOperators.MOD
        else -> ArifmeticOperators.NOT_AN_OPERATION
    }