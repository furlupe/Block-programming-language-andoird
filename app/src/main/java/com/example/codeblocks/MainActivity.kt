package com.example.codeblocks

import android.annotation.SuppressLint
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
import com.example.codeblocks.views.blocks.AssignVariableView
import com.example.codeblocks.views.blocks.CreateVariableView
import com.example.codeblocks.views.blocks.IfEndView
import com.example.codeblocks.views.blocks.IfStartView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    val code: MutableList<Command> = mutableListOf()

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.nav_create_var -> {
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
                }
                R.id.nav_assign_var -> {
                    val view = AssignVariableView(this)
                    val binding = com.example.codeblocks.databinding.AssignVariableViewBinding.bind(view)
                    val container = findViewById<LinearLayout>(R.id.container)
                    container.addView(view)

                    val operation = Assign(binding.variableName.text.toString(), binding.variableValue.text.toString())
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
                }

                R.id.nav_if -> {
                    val viewStart = IfStartView(this)
                    val viewEnd = IfEndView(this)
                    val binding = IfStartViewBinding.bind(viewStart)
                    val container = findViewById<LinearLayout>(R.id.container)
                    container.addView(viewStart)
                    container.addView(viewEnd)

                    val ifPlusCommand: Button = findViewById(R.id.if_plus_command)

                    val popupMenu1 = PopupMenu(this, ifPlusCommand)
                    popupMenu1.inflate(R.menu.activity_main_drawer)
                    popupMenu1.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.nav_create_var -> {
                                true
                            }
                            R.id.nav_assign_var -> {
                                true
                            }
                            else -> false
                        }
                    }

                    ifPlusCommand.setOnClickListener {
                        popupMenu1.show()
                    }

                    val signButton: Button = findViewById(R.id.button_sign)

                    val operation = If(binding.leftOperand.text.toString(), "=", binding.rightOperand.text.toString())

                    val popupMenu2 = PopupMenu(this, signButton)
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

                    binding.leftOperand.addTextChangedListener(object: TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            operation.assignLeft(binding.leftOperand.text.toString())
                        }

                    })

                    binding.rightOperand.addTextChangedListener(object: TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            operation.assignRight(binding.rightOperand.text.toString())
                        }

                    })
                    code.add(operation)
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

        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        when(item.itemId) {
            R.id.action_run ->{
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
}