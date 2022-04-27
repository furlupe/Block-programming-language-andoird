package com.example.codeblocks.model

import com.example.codeblocks.model.Comparators.*

interface Command {
    fun execute(_variables: MutableMap<String, Int>) {}
}

class Create(_name: String, _value: String) : Command {
    private val name: String = _name
    private val value: String = _value

    override fun execute(_variables: MutableMap<String, Int>) {
        if (_variables.containsKey(name)) {
            throw Exception("$name already exists.")
        }

        _variables[name] = Arifmetics.evaluateExpression(value, _variables)
    }
}

class Assign(_name: String, _value: String) : Command {
    private val name: String = _name
    private val value: String = _value

    override fun execute(_variables: MutableMap<String, Int>) {
        if (!_variables.containsKey(name)) {
            throw Exception("$name does not exist.")
        }

        _variables[name] = Arifmetics.evaluateExpression(value, _variables)
    }
}

open class If(_left: String, _comparator: String, _right: String, _commands: MutableList<Command>) :
    Command {

    private val inside: MutableList<Command> = _commands

    private val comparator: Comparators = getComparator(_comparator)
    private val left: String = _left
    private val right: String = _right

    fun checkIfExecutable(_variables: MutableMap<String, Int>): Boolean {
        val isExecutable: Boolean

        val countedLeft: Int = Arifmetics.evaluateExpression(left, _variables)
        val countedRight: Int = Arifmetics.evaluateExpression(right, _variables)

        isExecutable = when (comparator) {
            LESS -> (countedLeft < countedRight)
            GREATER -> (countedLeft > countedRight)
            EQUAL -> (countedLeft == countedRight)
            NOT_EQUAL -> (countedLeft != countedRight)
            LESS_OR_EQUAL -> (countedLeft <= countedRight)
            GREATER_OR_EQUAL -> (countedLeft >= countedRight)
        }

        return isExecutable
    }

    override fun execute(_variables: MutableMap<String, Int>) {

        if (checkIfExecutable(_variables)) {
            for (command in inside) {
                command.execute(_variables)
            }
        }
    }
}

// для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity,
// которая содержит в себе работу с textView из activity_main.xml
class Print(
    _toPrint: String,
    _showText: (toPrint: String, end: String) -> Unit,
    _end: String = "\n"
) : Command {

    private val toPrint: String = _toPrint
    private val end = _end
    private val showText: (toPrint: String, end: String) -> Unit = _showText

    override fun execute(_variables: MutableMap<String, Int>) {

        // проверяем, является ли переданное значени строкой ("что-то" или 'что-то')
        if (toPrint.matches("^(?:\"(?=.*\")|\'(?=.*\')).*".toRegex())) {
            // ...то вывести ее без кавычек
            showText(toPrint.substring(1, toPrint.length - 1), end)
        // иначе нам передали либо переменную, либо неправильно заданную строку
        } else {
            // если такой переменной нет, то выдаем ошибку
            if (!_variables.containsKey(toPrint)) {
                throw Exception("$toPrint does not exist")
            }
            // если есть, то выводим ее значение
            showText(_variables[toPrint].toString(), end)
        }
    }
}

// по аналогии с Print, в Input нужно передать лямбда-функцию из mainActivity,
// которая вызывает окно с текстовым полем для ввода данных и возвращает введенные данные
class Input(_variable: String, _inputText: () -> String) : Command {

    private val variable = _variable
    private val inputText: () -> String = _inputText

    override fun execute(_variables: MutableMap<String, Int>) {
        val value = inputText()
        if (!_variables.containsKey(variable)) {
            throw Exception("$variable does not exist")
        }
        _variables[variable] = Arifmetics.evaluateExpression(value, _variables)
    }
}

class While(_left: String, _comparator: String, _right: String, _commands: MutableList<Command>) :
    Command {
    private val inside = _commands

    private val left = _left
    private val right = _right
    private val comparator = _comparator

    override fun execute(_variables: MutableMap<String, Int>) {
        val isExecutable = If(left, comparator, right, inside).checkIfExecutable(_variables)

        if (isExecutable) {
            for (command in inside) {
                command.execute(_variables)
            }
            execute(_variables)
        }
    }
}