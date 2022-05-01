package com.example.codeblocks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeblocks.databinding.CommandDialogBinding
import com.example.codeblocks.model.*
import com.example.codeblocks.views.CreateVariableView


class MainActivity : AppCompatActivity() {
    val code: MutableList<Command> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ///////////////////////////////
    /*    val blockIdButton: Button = findViewById(R.id.blockIf)
        blockIdButton.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.block_if_dialog, null, false)
            val viewBinding = BlockIfDialogBinding.bind(view)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pick two variables and a comparator")
            builder.setView(view)

            builder.setPositiveButton("Create") { _, _ ->
                val left = viewBinding.varNameLeft.text.toString()
                val right = viewBinding.varNameRight.text.toString()
                val comparator = viewBinding.comparator.text.toString()

                makeIfCondition(left, comparator, right)
            }

            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        }*/

////////////////////////////////////////
        val addCommand: Button = findViewById(R.id.addCommand)
        addCommand.setOnClickListener{
            val view = layoutInflater.inflate(R.layout.command_dialog, null, false)
            val viewBinding = CommandDialogBinding.bind(view)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pick command to add")
            builder.setView(view)

            
        }

       /* val createButton: Button = findViewById(R.id.variables)
        createButton.setOnClickListener {
            val view = CreateVariableView(this)
            val binding = com.example.codeblocks.databinding.CreateVariableViewBinding.bind(view)
            val container = findViewById<LinearLayout>(R.id.container)
            container.addView(view)

            val operation = Variable(binding.variableName.text.toString(), binding.variableValue.text.toString())
            binding.variableName.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    operation.assignName(binding.variableName.text.toString())
                }

            })

            binding.variableValue.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    operation.assignValue(binding.variableValue.text.toString())
                }

            })
            code.add(operation)
        }*/


        val runButton: Button = findViewById(R.id.runButton)

        // для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity, которая содержит в себе работу с textView из activity_main.xml
        /*this.code.add( Print("\"some text\"") { toPrint: String ->

            val tv: TextView = findViewById(R.id.textView)

            var output: String = tv.text.toString()
            output += "\n$toPrint"

            tv.text = output

        })*/

        runButton.setOnClickListener {

            /* val op = If("a", "<", "b", mutableListOf())
            code.add( op )
            op.addCommandInside( Create("c", "15") ) */ // --> вот так добавлять команды в внутр. блоки

            Interpretator.run(code)
        }

    }
}