package com.example.codeblocks.model

class While(_condition: String, _commands: MutableList<Command> = mutableListOf()) :
    Command {

    override var name: String = ""
    override var pos = 0

    var condition = _condition
    val inside = _commands

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ){

        if(condition.isEmpty()) throw Exception("At: $pos\nEmpty condition for While")

        while (LogicalArifmetic.evalWhole(condition, _variables, _arrays)) {
            for (command in inside) {
                command.execute(_variables, _arrays)
            }
        }
    }
}