package com.example.codeblocks.model

import androidx.core.text.isDigitsOnly
import com.example.codeblocks.model.ArifmeticOperators.*

object Arifmetics {
    private fun createRPN(_expression: String): MutableList<String> {
        if (_expression.isEmpty()) {
            throw Exception("Присвойте переменной значение")
        }

        val expression = _expression.replace(" ", "") // изабвляемся от лишних пробелов

        val output: MutableList<String> = mutableListOf() // выходная строка
        val stack = ArrayDeque<ArifmeticOperators>() // стек для операторов

        var i = 0

        var openedBracket = false

        while (i < expression.length) {
            var c: String = expression[i].toString()
            // если прочитанный символ число или буква
            if (c[0].isLetterOrDigit() || c[0] == '_' || openedBracket) {
                // если число многоразрядное, или переменная имеет название длины > 1
                while (i + 1 < expression.length && (expression[i + 1].isLetterOrDigit() ||
                            (expression[i + 1] == '.')
                            || (expression[i + 1] == '[' || expression[i + 1] == '_') || openedBracket)
                ) {
                    openedBracket = when (expression[i + 1]) {
                        '[' -> true
                        ']' -> false
                        else -> openedBracket
                    }
                    i++
                    c += expression[i]
                }
                // добавить в выходную строку операнд
                output.add(c)

                // проверка на операторы
            } else {
                // получить ArifmeticOperator по значению строки
                when (val op = getArifmeticOperator(c)) {
                    OPEN_BRACKET -> stack.addLast(op)
                    CLOSED_BRACKET -> {
                        while (stack.last() != OPEN_BRACKET) {
                            // если стек опустел раньше нахождения "(", значит ариф. запись была не верной
                            if (stack.count() == 0) {
                                throw Exception("Wrong expression")
                            }
                            // Добавляем все с вершины стека, пока не найдем "("
                            output.add(stack.removeLast().operator)
                        }
                        stack.removeLast()
                    }
                    PLUS, FRACTION, MULTIPLY, MOD -> {

                        while (stack.count() > 0 && stack.last().priority >= op.priority) {
                            output.add(stack.removeLast().operator)
                        }
                        stack.addLast(op)
                    }
                    MINUS -> {
                        if (i == 0 || !expression[i - 1].toString().matches("[\\w\\[\\]\\)]".toRegex())) {
                            stack.addLast(UNARY_MINUS)
                        } else {
                            while (stack.count() > 0 && stack.last().priority >= op.priority) {
                                output.add(stack.removeLast().operator)
                            }
                            stack.addLast(op)
                        }
                    }

                    else -> throw Exception("$c is not an operator")
                }
            }
            i++
        }
        // заносим оставшиеся операции из стека в выходную строку
        while (stack.count() > 0) {
            val l = stack.removeLast()
            if (l == OPEN_BRACKET) throw Exception("Expression has inconsistent brackets")

            output.add(l.operator)
        }

        return output
    }

    // обработать арифметическое выражение
    fun evaluateExpression(
        expression: String,
        variables: MutableMap<String, Double>,
        arrays: MutableMap<String, MutableList<Double>>
    ): Double {
        val rpn = createRPN(expression)
        val stack = ArrayDeque<Double>()

        println(rpn)

        val doubleRegex = "^(?:[1-9]\\d*|0)\\.\\d+".toRegex()
        val variableRegex = "^[a-zA-Z_][a-zA-Z0-9_]*".toRegex()
        val arrayRegex = "^($variableRegex)\\[(.+)\\]".toRegex()

        for (operator in rpn) {
            if (operator.isDigitsOnly() || operator.matches(doubleRegex)) {
                stack.addLast(operator.toDouble())

                continue
            }

            if (operator.matches(variableRegex)) {
                if (!variables.containsKey(operator)) {
                    throw Exception("$operator does not exist")
                }
                val op = variables[operator] ?: throw Exception("$operator is null")
                stack.addLast(op)

                continue
            }

            if (operator.matches(arrayRegex)) {
                val (name, index) = arrayRegex.find(operator)!!.destructured
                if (!arrays.containsKey(name)) {
                    throw Exception("$operator does not exist")
                }
                val op = arrays[name]!![evaluateExpression(index, variables, arrays).toInt()]
                stack.addLast(op)

                continue
            }

            val op = getArifmeticOperator(operator)
            val a = stack.removeLast()

            if (op == UNARY_MINUS) {
                stack.addLast(-a)
                continue
            }

            val b = stack.removeLast()

            stack.addLast(when (op) {
                PLUS -> b + a
                MINUS -> b - a
                FRACTION -> b / a
                MULTIPLY -> b * a
                MOD -> b % a
                else -> throw Exception("$operator is not an operator")
            })

        }

        return stack.last()
    }

}
