package com.example.codeblocks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.codeblocks.databinding.*
import com.example.codeblocks.model.*
import com.example.codeblocks.views.blocks.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.jmedeisis.draglinearlayout.DragLinearLayout


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

        val container = findViewById<DragLinearLayout>(R.id.container)
        val scroll = findViewById<ScrollView>(R.id.scroll_view)
        container.setContainerScrollView(scroll)

        container.setOnViewSwapListener { firstView, firstPosition, secondView, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
            }
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
                        .setPositiveButton("OK") { _, _ -> }
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

    @SuppressLint("WrongViewCast")
    private fun addCreateVariableBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = CreateVariableView(context)

        container.addView(view)
        container.setViewDraggable(view, view)

        val binding = view.binding
        val operation = view.command

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(view.command)
        }

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

        view.accessory = list
        view.command.pos = container.indexOfChild(view)

        return operation
    }

    private fun addAssignVariableBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = AssignVariableView(context)

        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        val binding = view.binding

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

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

        view.accessory = list
        operation.pos = container.indexOfChild(view)

        return operation
    }

    private fun addIfBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = IfStartView(context)

        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        val binding = view.binding

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                operation.condition = binding.condition.text.toString()
            }

        })

        val inner_container = binding.ifContainer
        inner_container.setOnViewSwapListener { firstView, firstPosition, secondView, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
                println("${firstView.accessory}, ${firstView.command}")
            }
        }

        val addCommand: Button = binding.ifPlusCommand

        val popup = PopupMenu(context, addCommand)

        popup.inflate(R.menu.menu_plus_command_if)
        popup.setOnMenuItemClickListener {

            if (it.itemId == R.id.else_block) {
                if (!operation.elseExists) {
                    operation.elseExists = true
                    addElseToIf(
                        this,
                        operation
                    )
                }

            } else {
                val op = whichCommandToAdd(
                    it,
                    this,
                    inner_container,
                    operation.insideMainBlock
                )

                operation.insideMainBlock.add(op)
            }
            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        view.accessory = list
        operation.pos = container.indexOfChild(view)
        return operation
    }

    private fun addElseToIf(
        context: Context, myIf: If,
        container: RelativeLayout = findViewById(R.id.if_for_else_container),
        list: MutableList<Command> = code
    ) {
        val view = IfElseView(context)
        container.addView(view)

        val binding = view.binding

        val addCommand: Button = binding.elsePlusCommand

        val inner_container = binding.elseContainer
        inner_container.setOnViewSwapListener { firstView, firstPosition, secondView, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
                println("${firstView.accessory}, ${firstView.command}")
            }
        }

        val popupMenuElse = PopupMenu(context, addCommand)
        popupMenuElse.inflate(R.menu.menu_blocks_plus)
        popupMenuElse.setOnMenuItemClickListener {

            val op = whichCommandToAdd(
                it,
                this,
                inner_container,
                myIf.insideElseBlock
            )

            val delete:Button = binding.delete
            delete.setOnClickListener{
                myIf.elseExists = false
                container.removeView(view)
                myIf.insideElseBlock.clear()
            }

            myIf.insideElseBlock.add(op)
            true
        }

        addCommand.setOnClickListener {
            popupMenuElse.show()
        }

        view.accessory = list
    }

    fun addWhileBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = WhileStartView(context)

        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        val binding = view.binding

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

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
        val inner_container = binding.whileContainer

        inner_container.setOnViewSwapListener { firstView, firstPosition, secondView, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
                println("${firstView.accessory}, ${firstView.command}")
            }
        }

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_blocks_plus)
        popup.setOnMenuItemClickListener {

            val op = whichCommandToAdd(
                it,
                this,
                inner_container,
                operation.inside
            )

            operation.inside.add(op)

            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        view.accessory = list
        operation.pos = container.indexOfChild(view)
        return operation
    }

    fun addArrayBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = ArrayView(context)
        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        val binding = view.binding

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

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

        view.accessory = list
        operation.pos = container.indexOfChild(view)
        return operation
    }

    fun addPrintBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = PrintView(context)
        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        operation.showText = toPrintFunction

        val binding = view.binding

        val delete:Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

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

        view.accessory = list
        operation.pos = container.indexOfChild(view)

        return operation
    }

    fun addInputBlock(
        context: Context,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        val view = InputView(context)
        container.addView(view)
        container.setViewDraggable(view, view)

        val operation = view.command
        val binding = view.binding

        val delete: Button = binding.delete
        delete.setOnClickListener{
            container.removeView(view)
            list.remove(operation)
        }

        binding.inputTo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                operation.toInput = binding.inputTo.text.toString()
            }

        })

        view.accessory = list
        operation.pos = container.indexOfChild(view)

        return operation
    }

    private fun whichCommandToAdd(
        it: MenuItem,
        context: Context,
        container: DragLinearLayout,
        list: MutableList<Command>
    ) =
        when (it.itemId) {
            R.id.create_var -> addCreateVariableBlock(context, container, list)
            R.id.assign_var -> addAssignVariableBlock(context, container, list)
            R.id.if_block -> addIfBlock(context, container, list)
            R.id.while_block -> addWhileBlock(context, container, list)
            R.id.array_block -> addArrayBlock(context,  container, list)
            R.id.print_block -> addPrintBlock(context, container, list)
            else -> throw Exception("wtf")
        }
}

