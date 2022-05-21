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
import android.widget.RelativeLayout
import com.example.codeblocks.R
import com.example.codeblocks.databinding.IfStartViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.If
import com.example.codeblocks.model.Input
import com.jmedeisis.draglinearlayout.DragLinearLayout

class IfStartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = IfStartViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = If("")
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var prntFun: (toPrint: String, end: String) -> Unit
    override lateinit var inputD: (command: Input) -> Unit
    val innerContainer = binding.ifContainer

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

        val addCommand: Button = binding.ifPlusCommand
        val popup = PopupMenu(context, addCommand)

        popup.inflate(R.menu.menu_plus_command_if)
        popup.setOnMenuItemClickListener {

            if (it.itemId == R.id.else_block) {
                if (!command.elseExists) {
                    command.elseExists = true

                    val op = IfElseView(context)
                    op.prntFun = prntFun
                    op.inputD = inputD

                    op.init(container, command.insideElseBlock)
                    op.realCommand = command

                    findViewById<RelativeLayout>(R.id.if_for_else_container).addView(op)
                }

            } else {
                val op = addCommand(
                    it,
                    context
                )

                op.prntFun = prntFun
                op.inputD = inputD

                op.init(container, command.insideMainBlock)

                innerContainer.addView(op as View)
                innerContainer.setViewDraggable(op, op)

                command.insideMainBlock.add(op.command)
            }

            true
        }

        addCommand.setOnClickListener {
            popup.show()
        }

        accessory = list
        command.pos = container.indexOfChild(this)

    }
}