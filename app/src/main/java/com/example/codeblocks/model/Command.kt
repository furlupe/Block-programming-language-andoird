package com.example.codeblocks.model

import android.widget.TextView
import java.lang.Exception

import com.example.codeblocks.model.Comparators.*

interface Command {
    fun execute(_variables: MutableMap<String, Int>) {}
}

class Create(_name: String, _value: String): Command {
    private val name: String = _name
    private val value: String = _value

    override fun execute(_variables: MutableMap<String, Int>) {
        if (_variables.containsKey(this.name)) {
            throw Exception("${this.name} already exists.")
        }

        _variables[this.name] = Arifmetics.evaluateExpression(value, _variables)
    }
}

class Assign(_name: String, _value: String): Command {
    private val name: String = _name
    private val value: String = _value

    override fun execute(_variables: MutableMap<String, Int>) {
        if (! _variables.containsKey(this.name)) {
            throw Exception("${this.name} does not exist.")
        }

        _variables[this.name] = Arifmetics.evaluateExpression(value, _variables)
    }
}

class If(_comparator: String, _left: String, _right: String, _commands: MutableList<Command>): Command {

    private val inside: MutableList<Command> = _commands

    private val comparator: Comparators = getComparator(_comparator)
    private val left: String = _left
    private val right: String = _right

    override fun execute(_variables: MutableMap<String, Int>) {
        var isExecutable: Boolean = false

        val countedLeft: Int = Arifmetics.evaluateExpression(this.left, _variables)
        val countedRight: Int = Arifmetics.evaluateExpression(this.right, _variables)

        when(this.comparator) {
            LESS -> isExecutable = (countedLeft < countedRight)
            GREATER -> isExecutable = (countedLeft > countedRight)
            EQUAL -> isExecutable = (countedLeft == countedRight)
            NOT_EQUAL -> isExecutable = (countedLeft != countedRight)
            LESS_OR_EQUAL -> isExecutable = (countedLeft <= countedRight)
            GREATER_OR_EQUAL -> isExecutable = (countedLeft >= countedRight)
        }

        if (isExecutable == true) {
            for(command in this.inside) {
                command.execute(_variables)
            }
        }
    }
}

class Print(_toPrint: String, _showText: (toPrint: String) -> Unit): Command {

    private val toPrint: String = _toPrint
    private val showText: (toPrint: String) -> Unit = _showText

    override fun execute(_variables: MutableMap<String, Int>) {

        // если передали просто строку, которую нужно вывести (т.е. та строка, что имеет в себе символы откр. и закр. (") )...
        if (this.toPrint.matches("^\".*\"".toRegex())) {
            // ...то вывести ее без кавычек
            this.showText(this.toPrint.substring(1, this.toPrint.length - 1))
        // иначе нам передали либо переменную, либо неправильно заданную строку
        } else {
            // если такой переменной нет, то выдаем ошибку
            if(! _variables.containsKey(this.toPrint)) {
                throw Exception("${this.toPrint} does not exist")
            }
            // если есть, то выводим ее значение
            this.showText(_variables[this.toPrint].toString())
        }
    }
}