package com.example.codeblocks.model

import com.example.codeblocks.model.LogicOperators.*
import com.example.codeblocks.model.Comparators.*

object LogicalArifmetic {
    val logicops = mutableListOf('|', '&', '~', '(', ')', '_')
    val exprRegex = "(\\w+)(?:([!><]=|[><=])(\\w+))?"
    val logRegex = "[|&~_]".toRegex()

    fun parseExpr(_expr: String): MutableList<String> {
        val expr = _expr.replace(" ", "")

        var c: String = ""
        val output = mutableListOf<String>()
        val stack = ArrayDeque<Char>()

        for(i in expr) {
            if (i !in logicops)
            {
                c += i
            }
            else {
                if(c.isNotEmpty())
                    output.add(c)
                c = ""
                println(stack)
                when(val op = getLogicOperator(i)) {
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
                    OR, AND, EQUAL_LOGIC, NEGATE -> {
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
                    else -> throw Exception("Logic parsing error")
                }
            }
        }
        if(c.isNotEmpty()){
            output.add(c)
        }
        while (stack.count() > 0) {
            val l = stack.removeLast()
            if (getLogicOperator(l) == OPEN_BRACKET) throw Exception("Expression has inconsistent brackets")
            output.add(l.toString())
        }
        return output
    }

    fun evalExpr(_expr: String, _variables: MutableMap<String, Double>): Boolean {
        val (left, comp, right) = exprRegex.toRegex().find(_expr)!!.destructured
        val output: Boolean

        val countedLeft = Arifmetics.evaluateExpression(left, _variables)

        if (comp.isNotEmpty()) {
            val countedRight = Arifmetics.evaluateExpression(right, _variables)
            output = when(val op = getComparator(comp)) {
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

    fun evalWhole(_expr: String, _variables: MutableMap<String, Double>): Boolean {
        val expr = parseExpr(_expr)
        val stack = ArrayDeque<Boolean>()

        for(operator in expr) {
            if (!operator.matches(logRegex)) {
                stack.addLast(evalExpr(operator, _variables))
                continue
            }

            val op = getLogicOperator(operator[0])
            val a: Boolean = stack.removeLast()
            val b: Boolean
            println(expr)
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
                EQUAL_LOGIC -> {
                    b = stack.removeLast()
                    stack.addLast(a == b)
                    println("$a, $b, ${a==b}")
                }
                else -> throw Exception("Logic error")
            }
        }
        return stack.last()
    }
}