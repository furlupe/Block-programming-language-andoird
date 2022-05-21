package com.example.codeblocks.model

open class If(
    _condition: String,
    _commands: MutableList<Command> = mutableListOf(),
    _else: MutableList<Command> = mutableListOf()
) :
    Command {

    override var name = ""
    override var pos = 0

    var elseExists = false

    val insideMainBlock = _commands
    val insideElseBlock = _else
    var condition = _condition

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {

        if (condition.isEmpty()) throw Exception("At: $pos\nEmpty condition for If")

        val toExecute = if (LogicalArifmetic.evalWhole(
                condition,
                _variables,
                _arrays
            )
        ) insideMainBlock else insideElseBlock
        println(toExecute)
        for (command in toExecute)
            command.execute(_variables, _arrays)
    }
}