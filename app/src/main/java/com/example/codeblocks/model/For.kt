package com.example.codeblocks.model

class For(
    _before: MutableList<Command> = mutableListOf(),
    _condition: String = "",
    _eachStep: MutableList<Command> = mutableListOf(),
    _inside: MutableList<Command> = mutableListOf()
) : Command {
    override var name = ""
    override var pos = 0

    var before = _before
    var condition = _condition
    var eachStep = _eachStep
    var inside = _inside

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){
        val toDelete = mutableListOf<Command>()
        for (b in before) {
            b.execute(_variables, _arrays)
            if (b is Variable)
                toDelete.add(b)
        }

        while (LogicalArifmetic.evalWhole(condition, _variables, _arrays)) {
            for (i in inside + eachStep)
                i.execute(_variables, _arrays)
        }

        for (d in toDelete)
            _variables.remove(d.name)

    }

}