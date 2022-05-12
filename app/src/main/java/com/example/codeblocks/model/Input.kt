package com.example.codeblocks.model

// по аналогии с Print, в Input нужно передать лямбда-функцию из mainActivity,
// которая вызывает окно с текстовым полем для ввода данных и возвращает введенные данные
class Input(_name: String, _inputText: () -> String) : Command {

    override var name = _name
    private val inputText: () -> String = _inputText

    private val variableRegex = "^[a-zA-Z][a-zA-Z0-9]*".toRegex()
    private val arrayRegex = "^($variableRegex)\\[(.+)]".toRegex()

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){
        val value = inputText()
        if (name.matches(variableRegex)) {
            if (!_variables.containsKey(name)) throw Exception("Variable doesn't exist!")

            _variables[name] = Arifmetics.evaluateExpression(value, _variables, _arrays)
        }

        if (name.matches(arrayRegex)) {
            val (name, index) = arrayRegex.find(name)!!.destructured

            _arrays[name]!![Arifmetics.evaluateExpression(index, _variables, _arrays).toInt()] =
                Arifmetics.evaluateExpression(value, _variables, _arrays)
        }

    }
}