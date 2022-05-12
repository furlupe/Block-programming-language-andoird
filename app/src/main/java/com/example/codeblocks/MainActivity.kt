package com.example.codeblocks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.codeblocks.databinding.*
import com.example.codeblocks.model.*
import com.example.codeblocks.views.blocks.*
import com.google.android.material.navigation.NavigationView

const val PADDING = 110


class MainActivity : AppCompatActivity() {

    var code: MutableList<Command> = mutableListOf()
    val toPrintFunction = { toPrint: String, end: String ->
        val tv: TextView = findViewById(R.id.textView)

        var output = tv.text.toString()
        output += "$toPrint$end"

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            code.add(
                when (it.itemId) {
                    R.id.nav_create_var -> addCreateVariableBlock(this)
                    R.id.nav_assign_var -> addAssignVariableBlock(this)
                    R.id.nav_if -> addIfBlock(this)
                    R.id.nav_while -> addWhileBlock(this)
                    else -> throw Exception("wtf")
                }
            )
            true
        }

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
            R.id.action_clear -> {
                val container = findViewById<LinearLayout>(R.id.container)
                code.clear()
                container.removeAllViews()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun addCreateVariableBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = CreateVariableView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = Variable("")
        val binding = CreateVariableViewBinding.bind(view)

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

    private fun addAssignVariableBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = AssignVariableView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = Assign("", "")
        val binding = AssignVariableViewBinding.bind(view)

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

    private fun addIfBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = IfStartView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = If("")
        val binding = IfStartViewBinding.bind(view)
        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.changeCondition(binding.condition.text.toString())
            }

        })

        val addCommand: Button = binding.ifPlusCommand

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_plus_command_if)
        popup.setOnMenuItemClickListener {

            if (it.itemId == R.id.else_block) {
                operation.elseExists = true
                addElseToIf(
                    this,
                    operation,
                    multiplier,
                    container.indexOfChild(view) + countAmountOfViews(operation.insideMainBlock) + 1
                )

            } else {
                operation.addCommandInsideMainBlock(
                    whichCommandToAdd(
                        it,
                        this,
                        multiplier,
                        container.indexOfChild(view) + countAmountOfViews(operation.insideMainBlock) + 1
                    )
                )
            }
            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        return operation
    }

    private fun addElseToIf(context: Context, myIf: If, multiplier: Int = 0, index: Int = -1) {
        val view = IfElseView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val binding = IfElseViewBinding.bind(view)

        val addCommand: Button = binding.elsePlusCommand

        val popupMenuElse = PopupMenu(context, addCommand)
        popupMenuElse.inflate(R.menu.menu_blocks_plus)
        popupMenuElse.setOnMenuItemClickListener {

            myIf.addCommandInsideElseBlock(
                whichCommandToAdd(
                    it,
                    this,
                    multiplier,
                    container.indexOfChild(view) + 1
                )
            )
            true
        }

        addCommand.setOnClickListener {
            popupMenuElse.show()
        }
    }

    fun addWhileBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = WhileStartView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = While("")
        val binding = WhileStartViewBinding.bind(view)
        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.changeCondition(binding.condition.text.toString())
            }

        })

        val addCommand: Button = binding.whilePlusCommand

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_blocks_plus)
        popup.setOnMenuItemClickListener {

            operation.addCommandInside(
                whichCommandToAdd(
                    it,
                    this,
                    multiplier,
                    container.indexOfChild(view) + countAmountOfViews(operation.inside) + 1
                )
            )
            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        return operation
    }

    private fun whichCommandToAdd(it: MenuItem, context: Context, m: Int = 0, index: Int) =
        when (it.itemId) {
            R.id.create_var -> addCreateVariableBlock(context, m + 1, index)
            R.id.assign_var -> addAssignVariableBlock(context, m + 1, index)
            R.id.if_block -> addIfBlock(context, m + 1, index)
            R.id.while_block -> addWhileBlock(context, m + 1, index)
//            R.id.array_block -> add
            else -> throw Exception("wtf")
        }

    private fun countAmountOfViews(commands: MutableList<Command>): Int {
        var l = 0
        for (command in commands) {
            l += 1 + when (command) {
                is If -> countAmountOfViews(command.insideMainBlock) + countAmountOfViews(command.insideElseBlock) + if (command.elseExists) 1 else 0
                is While -> countAmountOfViews(command.inside)
                else -> 0
            }
        }

        return l
    }
}

