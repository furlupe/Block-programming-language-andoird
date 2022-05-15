package com.example.codeblocks.model

// для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity,
// которая содержит в себе работу с textView из activity_main.xml
class Print(
    _showText: (toPrint: String, end: String) -> Unit,
    _toPrint: String = "",
    _end: String = " "
) : Command {

    override var name = ""

    var toPrint = _toPrint.split("\\s*,\\s*".toRegex())
    var end = _end
    private val showText: (toPrint: String, end: String) -> Unit = _showText

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {

        // проверяем, является ли переданное значени строкой ("что-то" или 'что-то')
        for (out in toPrint) {
            if (out.matches("^(?:\"(?=.*\")|\'(?=.*\')).*".toRegex())) {
                // ...то вывести ее без кавычек
                showText(out.substring(1, out.length - 1), end)
                // иначе нам передали либо переменную, либо ариф. выражение, либо неправильную строку
            } else {
                // если есть, то выводим ее значение
                showText(Arifmetics.evaluateExpression(out, _variables, _arrays).toString(), end)
            }
        }
        showText("", "\n")
    }
}