package com.example.codeblocks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.codeblocks.databinding.ActivityMainBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.example.codeblocks.model.Interpretator
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
                whichCommandToAdd(when (it.itemId) {
                    R.id.nav_create_var -> CreateVariableView(this)
                    R.id.nav_assign_var -> AssignVariableView(this)
                    R.id.nav_if -> IfStartView(this)
                    R.id.nav_while -> WhileStartView(this)
                    R.id.nav_array -> ArrayView(this)
                    R.id.nav_print -> PrintView(this)
                    R.id.nav_input -> InputView(this)
                    else -> throw Exception("wtf")
                } as Block, )
            )
            true
        }

        val container = findViewById<DragLinearLayout>(R.id.container)
        val scroll = findViewById<ScrollView>(R.id.scroll_view)
        container.setContainerScrollView(scroll)

        container.setOnViewSwapListener { firstView, _, _, secondPosition ->
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

    private fun whichCommandToAdd(
        block: Block,
        container: DragLinearLayout = findViewById(R.id.container),
        list: MutableList<Command> = code
    ): Command {
        container.addView(block as View)
        container.setViewDraggable(block, block)

        block.prntFun = toPrintFunction
        block.inputD = inputDialog

        block.init(container, list)

        return block.command
    }

    val inputDialog = {command: Input ->
        val dialog = LayoutInflater.from(this).inflate(R.layout.input_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle(R.string.input)

        val dialogShow = builder.show()
        var out = ""

        dialog.findViewById<Button>(R.id.inputSend).setOnClickListener {
            command.value = dialog.findViewById<EditText>(R.id.input).text.toString()
            dialogShow.dismiss()
        }
    }

}

