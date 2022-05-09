package com.example.codeblocks.model

import com.example.codeblocks.model.Comparators.*
import com.example.codeblocks.model.LogicOperators.*

object LogicalArifmetic {
    private val logicops = mutableListOf('|', '&', '~', '(', ')')
    private val arifExpr = "[\\w\\+\\-\\*\\/\\%\\s]+".toRegex()
    private val exprRegex = "($arifExpr)(?:([><]=|[><=])($arifExpr))?".toRegex()
    private val logRegex = "[|&~]".toRegex()

    private fun parseExpr(_expr: String): MutableList<String> {
        val expr = _expr.replace(" ", "")

        var c = ""
        val output = mutableListOf<String>()
        val stack = ArrayDeque<LogicOperators>()

        var openedBrackets = false

        for (i in expr) {
            if (i !in logicops || openedBrackets) {
                c += i
                openedBrackets = when (i) {
                    '[' -> true
                    ']' -> false
                    else -> openedBrackets
                }
            } else {
                if (c.isNotEmpty())
                    output.add(c)
                c = ""
                when (val op = getLogicOperator(i)) {
                    OPEN_BRACKET -> stack.addLast(OPEN_BRACKET)
                    CLOSED_BRACKET -> {
                        while (stack.last() != OPEN_BRACKET) {
                            if (stack.count() == 0) {
                                throw Exception("Wrong expression")
                            }
                            output.add(stack.removeLast().operator)
                        }
                        stack.removeLast()
                    }
                    OR, AND, NEGATE -> {
                        while (stack.count() > 0 && stack.last().priority >= op.priority) {
                            output.add(stack.removeLast().operator)
                        }
                        stack.addLast(op)
                    }
                    NOT_AN_OPERATOR -> throw Exception("$i is not and operator")
                }
            }
        }
        if (c.isNotEmpty())
            output.add(c)

        while (stack.count() > 0) {
            val l = stack.removeLast()
            if (l == OPEN_BRACKET) throw Exception("Expression has inconsistent brackets")
            output.add(l.operator)
        }
        return output
    }

    private fun evalExpr(
        _expr: String,
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ): Boolean {
        val (left, comp, right) = exprRegex.find(_expr)!!.destructured
        val countedLeft = Arifmetics.evaluateExpression(left, _variables, _arrays)


        if (comp.isNotEmpty()) {
            val countedRight = Arifmetics.evaluateExpression(right, _variables, _arrays)
            return when (getComparator(comp)) {
                LESS -> (countedLeft < countedRight)
                GREATER -> (countedLeft > countedRight)
                LESS_OR_EQUAL -> (countedLeft <= countedRight)
                GREATER_OR_EQUAL -> (countedLeft >= countedRight)
                EQUAL -> (countedLeft == countedRight)
                NOT_EQUAL -> (countedLeft != countedRight)
            }
        }

        return (countedLeft > 0)
    }

    fun evalWhole(
        _expr: String,
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ): Boolean {
        val expr = parseExpr(_expr)
        val stack = ArrayDeque<Boolean>()

        for (operator in expr) {
            if (!operator.matches(logRegex)) {
                stack.addLast(evalExpr(operator, _variables, _arrays))
                continue
            }

            val op = getLogicOperator(operator[0])
            val a: Boolean = stack.removeLast()
            val b: Boolean

            stack.addLast(
                when (op) {
                    NEGATE -> !a
                    OR -> {
                        b = stack.removeLast()
                        a || b
                    }
                    AND -> {
                        b = stack.removeLast()
                        a && b
                    }
                    else -> throw Exception("Logic error")
                }
            )
        }
        return stack.last()
    }
}