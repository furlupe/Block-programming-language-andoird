package com.example.codeblocks.model

open class If(
    _condition: String,
    _commands: MutableList<Command> = mutableListOf(),
    _else: MutableList<Command> = mutableListOf()
) :
    Command {

    override var name = ""

    var elseExists = false

    val insideMainBlock = _commands
    val insideElseBlock = _else
    private var condition = _condition

    fun addCommandInsideMainBlock(_command: Command) {
        insideMainBlock.add(_command)
    }

    fun addCommandInsideElseBlock(_command: Command) {
        insideElseBlock.add(_command)
    }

    fun changeCondition(_condition: String) {
        condition = _condition
    }

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        val toExecute = if (LogicalArifmetic.evalWhole(
                condition,
                _variables,
                _arrays
            )
        ) insideMainBlock else insideElseBlock

        for (command in toExecute)
            command.execute(_variables, _arrays)
    }
}