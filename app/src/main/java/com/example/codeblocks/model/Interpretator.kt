package com.example.codeblocks.model

object Interpretator {
    // словарь переменных и массивов, которые создаются по ходу действия программы
    private val variables: MutableMap<String, Double> = mutableMapOf()
    private val arrays: MutableMap<String, MutableList<Double>> = mutableMapOf()

    // выполнить код
    fun run(code: MutableList<Command>) {
        println(code)
        for (command in code) {
            command.execute(variables, arrays)
        }
        println("$variables, $arrays")
        cleanse()
    }

    fun cleanse() {
        this.variables.clear()
        this.arrays.clear()
    }
}