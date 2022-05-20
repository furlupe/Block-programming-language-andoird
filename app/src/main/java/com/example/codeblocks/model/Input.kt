package com.example.codeblocks.model

// по аналогии с Print, в Input нужно передать лямбда-функцию из mainActivity,
// которая вызывает окно с текстовым полем для ввода данных и возвращает введенные данные
class Input(_toInput: String = "", _value: String = "") : Command {

    override var name = ""
    override var pos = 0

    val variableRegex = "^[a-zA-Z_][a-zA-Z0-9_]*".toRegex()
    val arrayRegex = "^($variableRegex)\\[(.+)\\]".toRegex()

    var toInput = _toInput
    var value = _value

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        if(toInput.isEmpty())
            throw Exception("At: $pos\nNowhere to input!")

        if(toInput.matches(variableRegex)) {
            if (!_variables.containsKey(toInput))
                throw Exception("At: $pos\nVar $toInput does not exist")
            _variables[toInput] = Arifmetics.evaluateExpression(value, _variables, _arrays)
        }

        if(toInput.matches(arrayRegex)) {
            val (arrayName, nonProcessedIndex) = arrayRegex.find(toInput)!!.destructured
            val index = Arifmetics.evaluateExpression(nonProcessedIndex, _variables, _arrays).toInt()

            if (!_arrays.containsKey(arrayName))
                throw Exception("At: $pos\nArray $toInput does not exist")

            _arrays[arrayName]!![index] = Arifmetics.evaluateExpression(value, _variables, _arrays)
        }
    }
}