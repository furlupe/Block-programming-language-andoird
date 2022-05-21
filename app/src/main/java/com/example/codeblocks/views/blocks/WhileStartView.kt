package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.example.codeblocks.R
import com.example.codeblocks.databinding.WhileStartViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.example.codeblocks.model.While
import com.jmedeisis.draglinearlayout.DragLinearLayout

class WhileStartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = WhileStartViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = While("")
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var prntFun: (toPrint: String, end: String) -> Unit
    override lateinit var inputD: (command: Input) -> Unit

    val innerContainer = binding.whileContainer

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {
        val delete: Button = binding.delete
        delete.setOnClickListener {
            container.removeView(this)
            list.remove(command)
        }

        binding.condition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                command.condition = binding.condition.text.toString()
            }

        })

        innerContainer.setOnViewSwapListener { firstView, _, _, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
                println("${firstView.accessory}, ${firstView.command}")
            }
        }

        val addCommand: Button = binding.whilePlusCommand
        val innerContainer = binding.whileContainer

        val popup = PopupMenu(context, addCommand)
        popup.inflate(R.menu.menu_blocks_plus)
        popup.setOnMenuItemClickListener {

            val op = addCommand(
                it,
                context
            )

            op.prntFun = prntFun
            op.inputD = inputD

            op.init(innerContainer, command.inside)
            command.inside.add(op.command)

            innerContainer.addView(op as View)
            innerContainer.setViewDraggable(op, op)

            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        accessory = list
        command.pos = container.indexOfChild(this)
    }

}