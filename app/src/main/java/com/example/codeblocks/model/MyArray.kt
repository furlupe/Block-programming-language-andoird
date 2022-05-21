package com.example.codeblocks.model

class MyArray(_name: String = "", _size: String = "", _inside: String = "") : Command {
    override var name = _name
    override var pos = 0

    var size = _size
    var inside = _inside

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {
        val prSize = Arifmetics.evaluateExpression(size, _variables, _arrays).toInt()
        val prInside = inside.split("\\s*,\\s*".toRegex())

        if (name[0].isDigit())
            throw Exception("At: $pos\nWrong name: $name")

        when (name) {
            in _variables, in _arrays -> throw Exception("At: $pos\n$name already exists!")
            "" -> throw Exception("At: $pos\nEmpty name")
        }

        if (prInside.count() != prSize)
            throw Exception("At: $pos\nItem's amount do not match the size!")

        _arrays[name] = mutableListOf()
        for (v in prInside) {
            _arrays[name]!!.add(Arifmetics.evaluateExpression(v, _variables, _arrays))
        }
    }

}