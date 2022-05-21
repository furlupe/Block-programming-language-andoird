package com.example.codeblocks.model

interface Command {

    var name: String
    var pos: Int

    fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {
    }

}