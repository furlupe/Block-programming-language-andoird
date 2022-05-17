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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.codeblocks.databinding.*
import com.example.codeblocks.model.*
import com.example.codeblocks.views.blocks.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView

const val PADDING = 110

class MainActivity : AppCompatActivity() {

    var code: MutableList<Command> = mutableListOf()

    val toPrintFunction = { toPrint: String, end: String ->
        val tv: TextView = findViewById(R.id.print)

        var output = tv.text.toString()
        output += toPrint + (end.ifEmpty { "\n" })

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

        val console = findViewById<FrameLayout>(R.id.console)

        BottomSheetBehavior.from(console).apply {
            peekHeight = 70
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

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
                    R.id.nav_array -> addArrayBlock(this)
                    R.id.nav_print -> addPrintBlock(this)
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
                val print: TextView = findViewById(R.id.print)
                print.text = ""

                try {
                    Interpretator.run(code)
                } catch (e: Exception) {
                    val errorBuilder = AlertDialog.Builder(this)
                    errorBuilder.setTitle("Error occurred")
                        .setMessage(e.message)
                        .setPositiveButton("OK") {_, _ -> Unit}
                        .show()
                }

                Interpretator.cleanse()
            }
            R.id.action_clear -> {
                val container = findViewById<LinearLayout>(R.id.container)
                container.removeAllViews()

                val print: TextView = findViewById(R.id.print)
                print.text = ""

                code.clear()
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
                operation.name = binding.variableName.text.toString()
            }

        })
        binding.variableValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.value = binding.variableValue.text.toString()
            }

        })

        operation.pos = container.indexOfChild(view)
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
                operation.name = binding.variableName.text.toString()
            }

        })
        binding.variableValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.value = binding.variableValue.text.toString()
            }

        })

        operation.pos = container.indexOfChild(view)
        return operation
    }

    private fun addIfBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = IfStartView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val viewEnd = IfEndView(context)
        viewEnd.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)
        container.addView(viewEnd, if (index > -1) index + 1 else container.childCount)

        val operation = If("")
        val binding = IfStartViewBinding.bind(view)
        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.condition = binding.condition.text.toString()
            }

        })

        val addCommand: Button = binding.ifPlusCommand

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_plus_command_if)
        popup.setOnMenuItemClickListener {

            if (it.itemId == R.id.else_block) {
                if (!operation.elseExists) {
                    operation.elseExists = true
                    addElseToIf(
                        this,
                        operation,
                        multiplier,
                        container.indexOfChild(view) + countAmountOfViews(operation.insideMainBlock) + 1
                    )
                }

            } else {
                operation.insideMainBlock.add(
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

        operation.pos = container.indexOfChild(view)
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

            myIf.insideElseBlock.add(
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

        val viewEnd = WhileEndView(context)
        viewEnd.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)
        container.addView(viewEnd, if (index > -1) index + 1 else container.childCount)

        val operation = While("")
        val binding = WhileStartViewBinding.bind(view)
        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.condition = binding.condition.text.toString()
            }

        })

        val addCommand: Button = binding.whilePlusCommand

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_blocks_plus)
        popup.setOnMenuItemClickListener {

            operation.inside.add(
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

        operation.pos = container.indexOfChild(view)
        return operation
    }

    fun addArrayBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = ArrayView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = MyArray("")
        val binding = ArrayViewBinding.bind(view)
        binding.arrayName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.name = binding.arrayName.text.toString()
            }

        })

        binding.size.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.size = binding.size.text.toString()
            }

        })

        binding.arrayValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.inside = binding.arrayValue.text.toString()
            }

        })

        operation.pos = container.indexOfChild(view)
        return operation
    }

    fun addPrintBlock(
        context: Context,
        multiplier: Int = 0,
        index: Int = -1
    ): Command {
        val view = PrintView(context)
        view.setPadding(PADDING * multiplier, 0, 0, 0)

        val container = findViewById<LinearLayout>(R.id.container)
        container.addView(view, if (index > -1) index else container.childCount)

        val operation = Print(toPrintFunction)
        val binding = PrintViewBinding.bind(view)
        binding.printTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.print = binding.printTo.text.toString()
            }

        })

        binding.printEnd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.end = binding.printEnd.text.toString()
            }

        })

        operation.pos = container.indexOfChild(view)
        return operation
    }

    private fun whichCommandToAdd(it: MenuItem, context: Context, m: Int = 0, index: Int) =
        when (it.itemId) {
            R.id.create_var -> addCreateVariableBlock(context, m + 1, index)
            R.id.assign_var -> addAssignVariableBlock(context, m + 1, index)
            R.id.if_block -> addIfBlock(context, m + 1, index)
            R.id.while_block -> addWhileBlock(context, m + 1, index)
            R.id.array_block -> addArrayBlock(context, m + 1, index)
            R.id.print_block -> addPrintBlock(context, m + 1, index)
            else -> throw Exception("wtf")
        }

    private fun countAmountOfViews(commands: MutableList<Command>): Int {
        var len = 0
        for (command in commands) {
            len += 1 + when (command) {
                is If -> 1 + countAmountOfViews(command.insideMainBlock) + countAmountOfViews(
                    command.insideElseBlock
                ) + if (command.elseExists) 1 else 0
                is While -> 1 + countAmountOfViews(command.inside)
                else -> 0
            }
        }

        return len
    }
}

