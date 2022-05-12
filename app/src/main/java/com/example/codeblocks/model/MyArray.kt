package com.example.codeblocks.model

class MyArray(_name: String, _size: String, _inside: String = "") : Command {
    override var name = _name
    private val nonProcessedSize = _size
    private val inside = _inside.split("\\s*,\\s*".toRegex())

    var size = 0

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){
        size = Arifmetics.evaluateExpression(nonProcessedSize, _variables, _arrays).toInt()

        if (_arrays.containsKey(name)) throw Exception("Variable doesn't exist!")
        if (inside.count() != size) throw Exception("Item's amount do not match the size!")

        _arrays[name] = mutableListOf()
        for (v in inside) {
            _arrays[name]!!.add(Arifmetics.evaluateExpression(v, _variables, _arrays))
        }
    }

}