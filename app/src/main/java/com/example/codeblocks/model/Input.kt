package com.example.codeblocks.model

// по аналогии с Print, в Input нужно передать лямбда-функцию из mainActivity,
// которая вызывает окно с текстовым полем для ввода данных и возвращает введенные данные
class Input(_toInput: String, _inputText: () -> String) : Command {

    override var name = ""

    private val variableRegex = "^[a-zA-Z][a-zA-Z0-9]*".toRegex()
    private val arrayRegex = "^($variableRegex)\\[(.+)]".toRegex()

    val toInput = _toInput.split("\\s*,\\s*".toRegex())
    val values = _inputText().split("\\s*,\\s*".toRegex())

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){
        if (toInput.count() != values.count())
            throw Exception("Expected more or less values to input")

        for(i in 0..toInput.count()) {
            if (toInput[i].matches(variableRegex)) {
                if (!_variables.containsKey(toInput[i])) throw Exception("Variable doesn't exist!")

                _variables[toInput[i]] = Arifmetics.evaluateExpression(values[i], _variables, _arrays)
            }

            if (toInput[i].matches(arrayRegex)) {
                val (n, index) = arrayRegex.find(toInput[i])!!.destructured

                _arrays[n]!![Arifmetics.evaluateExpression(index, _variables, _arrays).toInt()] =
                    Arifmetics.evaluateExpression(values[i], _variables, _arrays)
            }
        }
    }
}