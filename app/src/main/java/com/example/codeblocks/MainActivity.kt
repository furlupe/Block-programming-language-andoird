package com.example.codeblocks

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.codeblocks.databinding.AssignVariableDialogBinding
import com.example.codeblocks.databinding.BlockIfDialogBinding
import com.example.codeblocks.databinding.CreateVariableDialogBinding
import com.example.codeblocks.databinding.VariableViewBinding
import com.example.codeblocks.model.*


class MainActivity : AppCompatActivity() {
    val code: MutableList<Command> = mutableListOf()

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
            right: String
        ) {
            this.code.add( If(left, comparator, right) )
        }

        ///////////////////////////////

      /*  val plusButton: Button = findViewById(R.id.ifPlus)
        plusButton.setOnClickListener{
            val container = findViewById<LinearLayout>(R.id.container)
            val view = layoutInflater.inflate(R.layout.toggle_edit_text, null, false)
            container.addView(view)
*/

//            val editText1 = findViewById<EditText>(R.id.variable1)
//            Toast.makeText(this, editText1.text, Toast.LENGTH_SHORT).show()


        /*    val createButton1: Button = findViewById(R.id.toggle)
            createButton1.setOnClickListener{
                val editText1 = findViewById<EditText>(R.id.variable1)
                val name = editText1.text.toString()
                val editText2 = findViewById<EditText>(R.id.variable2)
                val value = editText2.text.toString()

                createVariable(name, value)
            }
        }*/

        val blockIdButton: Button = findViewById(R.id.blockIf)
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
        }

////////////////////////////////////////
        val createButton: Button = findViewById(R.id.variables)
        createButton.setOnClickListener {
            val view = VariableView(this)
            val binding = VariableViewBinding.bind(view)
            val container = findViewById<LinearLayout>(R.id.container)
            container.addView(view)

            val operation = Create(binding.variable1.text.toString(), binding.variable2.text.toString())
            binding.variable1.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    operation.assignName(binding.variable1.text.toString())
                }

            })

            binding.variable2.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    operation.assignValue(binding.variable2.text.toString())
                }

            })
            code.add(operation)
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