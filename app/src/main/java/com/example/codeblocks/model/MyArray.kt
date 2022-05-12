package com.example.codeblocks.model

class MyArray(_name: String, _size: String, _inside: String = "") : Command {
    override var name = _name
    private val nonProcessedSize = _size
    private val inside = _inside.split("\\s*,\\s*".toRegex())

    private var size = 0

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){
        if (_arrays.containsKey(name)) throw Exception("Variable doesn't exist!")

        size = Arifmetics.evaluateExpression(nonProcessedSize, _variables, _arrays).toInt()

        _arrays[name] = mutableListOf()
        for (v in inside) {
            _arrays[name]!!.add(Arifmetics.evaluateExpression(v, _variables, _arrays))
        }
    }

    fun addValue(
        _value: String,
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {
        if (_arrays[name]!!.size >= size) {
            throw Exception("Out of size")
        }
        _arrays[name]!!.add(Arifmetics.evaluateExpression(_value, _variables, _arrays))
    }

}