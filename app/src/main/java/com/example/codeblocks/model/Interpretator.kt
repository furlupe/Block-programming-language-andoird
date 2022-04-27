package com.example.codeblocks.model

object Interpretator {
    // словарь переменных, которые создаются по ходу действия программы
    private val variables: MutableMap<String, Int> = mutableMapOf()

    // выполнить код
    fun run(code: MutableList<Command>) {
        for (command in code) {
            command.execute(variables)
        }
        println(variables)
        this.variables.clear()
    }
}