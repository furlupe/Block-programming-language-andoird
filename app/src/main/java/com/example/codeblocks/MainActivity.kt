package com.example.codeblocks

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codeblocks.databinding.AssignVariableDialogBinding
import com.example.codeblocks.databinding.CreateVariableDialogBinding
import com.example.codeblocks.databinding.BlockIfDialogBinding
import com.example.codeblocks.model.*

class MainActivity : AppCompatActivity() {
    var code: MutableList<Command> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // добавить команду создания переменной
        fun createVariable(name: String, value: String) = this.code.add(Create(name, value))

        //  добавить команду присвоения нового значения
        fun assignVariable(name: String, value: String) = this.code.add(Assign(name, value))

        fun makeIfCondition(
            left: String,
            comparator: String,
            right: String,
            innerBlock: MutableList<Command>
        ) {
            this.code.add( If(left, comparator, right, innerBlock) )
        }

        val createButton: Button = findViewById(R.id.button)
        createButton.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.create_variable_dialog, null, false)
            val viewBinding = CreateVariableDialogBinding.bind(view)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pick a name and a value")
            builder.setView(view)

            builder.setPositiveButton("Create") { _, _ ->
                val name = viewBinding.varName.text.toString()
                val value = viewBinding.value.text.toString()

                createVariable(name, value)
            }

            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        }

        val assignButton: Button = findViewById(R.id.assign)
        assignButton.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.assign_variable_dialog, null, false)
            val viewBinding = AssignVariableDialogBinding.bind(view)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Write the name of the variable and new value for it")
            builder.setView(view)

            builder.setPositiveButton("Assign") { _, _ ->
                val name = viewBinding.varName.text.toString()
                val value = viewBinding.value.text.toString()

                assignVariable(name, value)
            }

            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        }

        val runButton: Button = findViewById(R.id.runButton)

        // для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity, которая содержит в себе работу с textView из activity_main.xml
        /*this.code.add( Print("\"some text\"") { toPrint: String ->

            val tv: TextView = findViewById(R.id.textView)

            var output: String = tv.text.toString()
            output += "\n$toPrint"

            tv.text = output

        })*/

        runButton.setOnClickListener {
            /*code.add(If("50", "<=", "60", mutableListOf(Print("\"Sussy\"", { toPrint: String, end: String ->

                val tv: TextView = findViewById(R.id.textView)

                var output: String = tv.text.toString()
                output += "$toPrint$end"

                tv.text = output

            }), Create("amogus", "60+50"))))
            code.add(Create("a", "0"))
            code.add(While("a", "<", "10", mutableListOf(Print("a", { toPrint: String, end: String ->

                val tv: TextView = findViewById(R.id.textView)

                var output: String = tv.text.toString()
                output += "$toPrint$end"

                tv.text = output

            }, " "), Assign("a", "a+1"))))
            code.add(Assign("a", "15"))
            code.add(Create("b", "a*2"))
            code.add(If("50", "<=", "60", mutableListOf(Print("\"Sussy new\"", { toPrint: String, end: String ->

                val tv: TextView = findViewById(R.id.textView)

                var output: String = tv.text.toString()
                output += "$toPrint$end"

                tv.text = output

            }), Create("fortnite", "60+50"))))*/

            /* val op = If("a", "<", "b", mutableListOf())
            code.add( op )
            op.addCommandInside( Create("c", "15") ) */ // --> вот так добавлять команды в внутр. блоки

            Interpretator.run(code)
        }

    }
}