package com.example.codeblocks.model

class Variable(_name: String, _value: String = "0") : Command {
    override var name = _name
    private var value = _value

    fun assignName(_name: String) {
        name = _name
    }

    fun assignValue(_value: String) {
        value = _value
    }

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        if (_variables.containsKey(name)) throw Exception("Variable already exists!")

        _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
    }
}