package com.example.codeblocks.model

// для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity,
// которая содержит в себе работу с textView из activity_main.xml
class Print(
    _showText: (toPrint: String, end: String) -> Unit,
    _toPrint: String = "",
    _end: String = "\n"
) : Command {

    override var name = ""
    override var pos = 0

    var print = _toPrint
    var end = _end
    private val showText: (toPrint: String, end: String) -> Unit = _showText

    override fun execute(
        _variables: MutableMap<String, Double>,
        _arrays: MutableMap<String, MutableList<Double>>
    ) {
        val toPrint = print.split("\\s*,\\s*".toRegex())
        for (out in toPrint) {
            if (out.matches("^(?:\"(?=.*\")|\'(?=.*\')).*".toRegex())) {
                showText(out.substring(1, out.length - 1), end)
            } else {
                showText(Arifmetics.evaluateExpression(out, _variables, _arrays).toString(), end)
            }
        }
        showText("", "\n")
    }
}