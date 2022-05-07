package com.example.codeblocks.model

import com.example.codeblocks.model.Comparators.*
import com.example.codeblocks.model.LogicOperators.*

object LogicalArifmetic {
    val logicops = mutableListOf('|', '&', '~', '(', ')')
    val variableOrArrayRegex = "\\w+(?:\\[.+\\])?".toRegex()
    val exprRegex = "($variableOrArrayRegex)(?:([><]=|[><=])($variableOrArrayRegex))?".toRegex()
    val logRegex = "[|&~]".toRegex()

    fun parseExpr(_expr: String): MutableList<String> {
        val expr = _expr.replace(" ", "")

        var c: String = ""
        val output = mutableListOf<String>()
        val stack = ArrayDeque<Char>()

        var openedBrackets = false

        for (i in expr) {
            if (i !in logicops || openedBrackets) {
                c += i
                openedBrackets = when(i) {
                    '[' -> true
                    ']' -> false
                    else -> openedBrackets
                }
            } else {
                if (c.isNotEmpty())
                    output.add(c)
                c = ""
                when (val op = getLogicOperator(i)) {
                    OPEN_BRACKET -> stack.addLast('(')
                    CLOSED_BRACKET -> {
                        while (stack.last() != '(') {
                            if (stack.count() == 0) {
                                throw Exception("Wrong expression")
                            }
                            output.add(stack.removeLast().toString())
                        }
                        stack.removeLast()
                    }
                    OR, AND, NEGATE -> {
                        if (stack.count() > 0) {
                            var stackOP = getLogicOperator(stack.last())
                            while (stackOP.priority >= op.priority && stack.count() > 0) {
                                output.add(stack.removeLast().toString())
                                if (stack.count() > 0) stackOP = getLogicOperator(stack.last())
                            }
                        }
                        stack.addLast(i)
                    }
                    NOT_AN_OPERATOR -> throw Exception("$i is not and operator")
                }
            }
        }
        if (c.isNotEmpty())
            output.add(c)

        while (stack.count() > 0) {
            val l = stack.removeLast()
            if (getLogicOperator(l) == OPEN_BRACKET) throw Exception("Expression has inconsistent brackets")
            output.add(l.toString())
        }
        return output
    }

    fun evalExpr(
        _expr: String,
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ): Boolean {
        val (left, comp, right) = exprRegex.find(_expr)!!.destructured
        val output: Boolean
        val countedLeft = Arifmetics.evaluateExpression(left, _variables, _arrays)


        if (comp.isNotEmpty()) {
            val countedRight = Arifmetics.evaluateExpression(right, _variables, _arrays)
            output = when (val op = getComparator(comp)) {
                LESS -> (countedLeft < countedRight)
                GREATER -> (countedLeft > countedRight)
                LESS_OR_EQUAL -> (countedLeft <= countedRight)
                GREATER_OR_EQUAL -> (countedLeft >= countedRight)
                EQUAL -> (countedLeft == countedRight)
                NOT_EQUAL -> (countedLeft != countedRight)
            }
            return output
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

        println(expr)

        for (operator in expr) {
            if (!operator.matches(logRegex)) {
                stack.addLast(evalExpr(operator, _variables, _arrays))
                continue
            }

            val op = getLogicOperator(operator[0])
            val a: Boolean = stack.removeLast()
            val b: Boolean
            when (op) {
                NEGATE -> {
                    stack.addLast(!a)
                }
                OR -> {
                    b = stack.removeLast()
                    stack.addLast(a || b)
                }
                AND -> {
                    b = stack.removeLast()
                    stack.addLast(a && b)
                }
                else -> throw Exception("Logic error")
            }
        }
        return stack.last()
    }
}