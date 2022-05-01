package com.example.codeblocks.model

import com.example.codeblocks.model.Comparators.*

interface Command {
    fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {}
}

class Variable(_name: String, _value: String) : Command {
    private val name: String = _name
    private val value: String = _value

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        if (_variables.containsKey(name)) {
            throw Exception("$name already exists.")
        }

        _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
    }
}

class MyArray(_name: String, _size: String, _inside: String = "") : Command{
    private val name = _name
    private val nonProcessedSize = _size
    private val inside = _inside.split("\\s*,\\s*".toRegex())

    private var size = 0

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        if (_arrays.containsKey(name)) {
            throw Exception("$name already exist")
        }

        size = Arifmetics.evaluateExpression(nonProcessedSize, _variables, _arrays).toInt()

        _arrays[name] = mutableListOf()
        for (v in inside) {
            _arrays[name]!!.add(Arifmetics.evaluateExpression(v, _variables, _arrays))
        }
    }

    fun addValue(_value: String, _variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        if (_arrays[name]!!.size >= size) {
            throw Exception("Out of size")
        }
        _arrays[name]!!.add(Arifmetics.evaluateExpression(_value, _variables, _arrays))
    }

}

class Assign(_name: String, _value: String) : Command {
    private val name: String = _name
    private val value: String = _value

    private val variableRegex = "^[a-zA-Z][a-zA-Z0-9]*".toRegex()
    private val arrayRegex = "^($variableRegex)\\[(\\w+)\\]".toRegex()

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        if (name.matches(variableRegex)) {
            if (!_variables.containsKey(name)) {
                throw Exception("$name does not exist.")
            }

            _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
            return
        }

        if (name.matches(arrayRegex)) {
            val (name, index) = arrayRegex.find(name)!!.destructured
            if (!_arrays.containsKey(name)) {
                throw Exception("$name does not exist")
            }

            _arrays[name]!![Arifmetics.evaluateExpression(index, _variables, _arrays).toInt()] = Arifmetics.evaluateExpression(value, _variables, _arrays)
            return
        }

        throw Exception("Invalid name: $name")

    }
}

open class If(_left: String, _comparator: String, _right: String, _commands: MutableList<Command> = mutableListOf()) :
    Command {

    private val inside: MutableList<Command> = _commands

    private val comparator: Comparators = getComparator(_comparator)
    private val left: String = _left
    private val right: String = _right

    fun addCommandInside(_command: Command) {
        inside.add(_command)
    }

    fun checkIfExecutable(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>): Boolean {
        val isExecutable: Boolean

        val countedLeft = Arifmetics.evaluateExpression(left, _variables, _arrays)
        val countedRight = Arifmetics.evaluateExpression(right, _variables, _arrays)

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

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {

        if (checkIfExecutable(_variables, _arrays)) {
            for (command in inside) {
                command.execute(_variables, _arrays)
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

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {

        // проверяем, является ли переданное значени строкой ("что-то" или 'что-то')
        if (toPrint.matches("^(?:\"(?=.*\")|\'(?=.*\')).*".toRegex())) {
            // ...то вывести ее без кавычек
            showText(toPrint.substring(1, toPrint.length - 1), end)
            // иначе нам передали либо переменную, либо ариф. выражение, либо неправильную строку
        } else {
            // если есть, то выводим ее значение
            showText(Arifmetics.evaluateExpression(toPrint, _variables, _arrays).toString(), end)
        }
    }
}

// по аналогии с Print, в Input нужно передать лямбда-функцию из mainActivity,
// которая вызывает окно с текстовым полем для ввода данных и возвращает введенные данные
class Input(_name: String, _inputText: () -> String) : Command {

    private val name = _name
    private val inputText: () -> String = _inputText

    private val variableRegex = "^[a-zA-Z][a-zA-Z0-9]*".toRegex()
    private val arrayRegex = "^($variableRegex)\\[(\\w+)\\]".toRegex()

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        val value = inputText()
        if (name.matches(variableRegex)) {
            if (!_variables.containsKey(name)) {
                throw Exception("$name does not exist.")
            }

            _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
            return
        }

        if (name.matches(arrayRegex)) {
            val (name, index) = arrayRegex.find(name)!!.destructured
            if (!_arrays.containsKey(name)) {
                throw Exception("$name does not exist")
            }

            _arrays[name]!![Arifmetics.evaluateExpression(index, _variables, _arrays).toInt()] = Arifmetics.evaluateExpression(value, _variables, _arrays)
            return
        }

        throw Exception("Invalid name: $name")

    }
}

class While(_left: String, _comparator: String, _right: String, _commands: MutableList<Command>) :
    Command {
    private val inside = _commands

    private val left = _left
    private val right = _right
    private val comparator = _comparator

    override fun execute(_variables: MutableMap<String, Double>, _arrays: MutableMap<String, MutableList<Double>>) {
        val isExecutable = If(left, comparator, right, inside).checkIfExecutable(_variables, _arrays)

        if (isExecutable) {
            for (command in inside) {
                command.execute(_variables, _arrays)
            }
            execute(_variables, _arrays)
        }
    }
}