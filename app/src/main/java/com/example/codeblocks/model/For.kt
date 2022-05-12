package com.example.codeblocks.model

class For(
    _before: MutableList<Command> = mutableListOf(),
    _condition: String = "",
    _eachStep: MutableList<Command> = mutableListOf(),
    _inside: MutableList<Command> = mutableListOf()
) : Command {
    override var name = ""

    private var before = _before
    fun addCommandToDoBefore(command: Command) = before.add(command)

    private var condition = _condition
    fun changeCondition(cond: String) {
        condition = cond
    }

    private var eachStep = _eachStep
    fun addCommandToDoEachStep(command: Command) = eachStep.add(command)

    private var inside = _inside
    fun addCommandToDoInside(command: Command) = inside.add(command)

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