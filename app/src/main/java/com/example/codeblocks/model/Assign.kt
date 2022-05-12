package com.example.codeblocks.model

class Assign(_name: String, _value: String) : Command {
    override var name: String = _name
    private var value: String = _value

    fun assignName(_name: String) {
        name = _name
    }

    fun assignValue(_value: String) {
        value = _value
    }

    private val variableRegex = "^[a-zA-Z][a-zA-Z0-9]*".toRegex()
    private val arrayRegex = "^($variableRegex)\\[(.+)]".toRegex()

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {

        if (name.matches(variableRegex)) {
            if (!_variables.containsKey(name)) throw Exception("Variable doesn't exist!")

            _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
        }

        if (name.matches(arrayRegex)) {
            val (arrName, index) = arrayRegex.find(name)!!.destructured
            if (!_arrays.containsKey(arrName)) throw Exception("Variable doesn't exist!")

            _arrays[arrName]!![Arifmetics.evaluateExpression(index, _variables, _arrays).toInt()] =
                Arifmetics.evaluateExpression(value, _variables, _arrays)
        }
    }
}