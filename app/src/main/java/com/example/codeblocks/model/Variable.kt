package com.example.codeblocks.model

class Variable(_name: String, _value: String = "0") : Command {
    override var name = _name
    override var pos = 0

    var value = _value

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        when(name) {
            in _variables, in _arrays -> throw Exception("At: $pos\n$name already exists")
            "" -> throw Exception("At: $pos\nEmpty name")
        }

        _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
    }
}