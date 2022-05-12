package com.example.codeblocks.model

class While(_condition: String, _commands: MutableList<Command> = mutableListOf()) :
    Command {

    override var name: String = ""

    private var condition = _condition
    val inside = _commands

    fun addCommandInside(_command: Command) {
        inside.add(_command)
    }

    fun changeCondition(_condition: String) {
        condition = _condition
    }

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        while (LogicalArifmetic.evalWhole(condition, _variables, _arrays)) {
            for (command in inside) {
                command.execute(_variables, _arrays)
            }
        }
    }
}