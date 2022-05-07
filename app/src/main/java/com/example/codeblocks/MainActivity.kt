package com.example.codeblocks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.codeblocks.databinding.ActivityMainBinding
import com.example.codeblocks.databinding.IfStartViewBinding
import com.example.codeblocks.model.*
import com.example.codeblocks.views.blocks.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    var code: MutableList<Command> = mutableListOf()
    val toPrintFunction = { toPrint: String, end: String ->
        val tv: TextView = findViewById(R.id.textView)

        var output = tv.text.toString()
        output += "$end$toPrint"

        tv.text = output
    }


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        fun makeIfCondition(
            condition: String,
            innerBlock: MutableList<Command>
        ) {
            this.code.add(If(condition, innerBlock))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_create_var -> {
                    code.add(addCreateVariableBlock(this))
                }
                R.id.nav_assign_var -> {
                    code.add(addAssignVariableBlock(this))
                }
                R.id.nav_if -> {
                    code.add(addIfBlock(this))
                }
            }
            true
        }

//        val runButton: Button = findViewById(R.id.runButton)

        // для выполнения кода Print, в него необходимо передать лямбда-функцию из mainActivity, которая содержит в себе работу с textView из activity_main.xml
        /*this.code.add( Print("\"some text\"") { toPrint: String ->

            val tv: TextView = findViewById(R.id.textView)

            var output: String = tv.text.toString()
            output += "\n$toPrint"

            tv.text = output

        })*/

//        runButton.setOnClickListener {

        /* val op = If("a", "<", "b", mutableListOf())
            code.add( op )
            op.addCommandInside( Create("c", "15") ) */ // --> вот так добавлять команды в внутр. блоки

//            Interpretator.run(code)
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.action_run -> {
                /* val op = If("a", "<", "b", mutableListOf())
                code.add( op )
                op.addCommandInside( Create("c", "15") ) */ // --> вот так добавлять команды в внутр. блоки

                Interpretator.run(code)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun addCreateVariableBlock(context: Context): Command {
        val view = CreateVariableView(context)
        val binding = com.example.codeblocks.databinding.CreateVariableViewBinding.bind(view)
        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view)

        val operation =
            Variable(binding.variableName.text.toString(), binding.variableValue.text.toString())
        binding.variableName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.assignName(binding.variableName.text.toString())
            }

        })

        binding.variableValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.assignValue(binding.variableValue.text.toString())
            }

        })
        return operation
    }

    fun addAssignVariableBlock(context: Context): Command {
        val view = AssignVariableView(context)
        val binding = com.example.codeblocks.databinding.AssignVariableViewBinding.bind(view)
        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view)

        val operation =
            Assign(binding.variableName.text.toString(), binding.variableValue.text.toString())
        binding.variableName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.assignName(binding.variableName.text.toString())
            }

        })

        binding.variableValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.assignValue(binding.variableValue.text.toString())
            }

        })
        return operation
    }

    fun addIfBlock(context: Context): Command {
        val viewStart = IfStartView(context)
        val viewEnd = IfEndView(context)
        val binding = IfStartViewBinding.bind(viewStart)
        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(viewStart)
        container.addView(viewEnd)

        val ifPlusCommand: Button = binding.ifPlusCommand

        val operation = If("")

        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.changeCondition(binding.condition.text.toString())
            }

        })

        val popupMenu1 = PopupMenu(context, ifPlusCommand)
        popupMenu1.inflate(R.menu.activity_main_drawer)
        popupMenu1.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.nav_create_var -> {
                    operation.addCommandInside(addCreateVariableBlock(this))
                    true
                }
                R.id.nav_assign_var -> {
                    operation.addCommandInside(addAssignVariableBlock(this))
                    true
                }
                R.id.nav_if -> {
                    println(viewStart.context)
                    operation.addCommandInside(addIfBlock(viewStart.context))
                    true
                }
                else -> false
            }
        }

        ifPlusCommand.setOnClickListener {
            popupMenu1.show()
        }

        /* val signButton: Button = binding.buttonSign

         val popupMenu2 = PopupMenu(context, signButton)
         popupMenu2.inflate(R.menu.popup_menu_comparison_sign)
         popupMenu2.setOnMenuItemClickListener {
             when (it.itemId) {
                 R.id.less -> {
                     operation.assignComparator("<")
                     signButton.text = "<"
                     true
                 }
                 R.id.greater -> {
                     operation.assignComparator(">")
                     signButton.text = ">"
                     true
                 }
                 R.id.equal -> {
                     operation.assignComparator("=")
                     signButton.text = "="
                     true
                 }
                 R.id.not_equal -> {
                     operation.assignComparator("!=")
                     signButton.text = "!="
                     true
                 }
                 R.id.less_or_equal -> {
                     operation.assignComparator("<=")
                     signButton.text = "<="
                     true
                 }
                 R.id.greater_or_equal -> {
                     operation.assignComparator(">=")
                     signButton.text = ">="
                     true
                 }
                 else -> false
             }
         }

         signButton.setOnClickListener {
             popupMenu2.show()
         }

         binding.leftOperand.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(
                 p0: CharSequence?,
                 p1: Int,
                 p2: Int,
                 p3: Int
             ) {
             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
             }

             override fun afterTextChanged(p0: Editable?) {
                 operation.assignLeft(binding.leftOperand.text.toString())
             }

         })

         binding.rightOperand.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(
                 p0: CharSequence?,
                 p1: Int,
                 p2: Int,
                 p3: Int
             ) {
             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
             }

             override fun afterTextChanged(p0: Editable?) {
                 operation.assignRight(binding.rightOperand.text.toString())
             }

         })*/
        return operation
    }
}
