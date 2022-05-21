package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.example.codeblocks.R
import com.example.codeblocks.databinding.IfElseViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.If
import com.example.codeblocks.model.Input
import com.jmedeisis.draglinearlayout.DragLinearLayout

class IfElseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = IfElseViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = If("")
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var prntFun: (toPrint: String, end: String) -> Unit
    override lateinit var inputD: (command: Input) -> Unit

    val innerContainer = binding.elseContainer
    var realCommand = If("")

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {

        innerContainer.setOnViewSwapListener { firstView, _, _, secondPosition ->
            if (firstView is Block) {
                firstView.accessory.remove(firstView.command)
                firstView.accessory.add(secondPosition, firstView.command)
                println("${firstView.accessory}, ${firstView.command}")
            }
        }

        val addCommand: Button = binding.elsePlusCommand

        val popupMenuElse = PopupMenu(context, addCommand)
        popupMenuElse.inflate(R.menu.menu_blocks_plus)
        popupMenuElse.setOnMenuItemClickListener {

            val op = addCommand(
                it,
                context
            )

            op.prntFun = prntFun
            op.inputD = inputD

            op.init(innerContainer, list)

            val delete: Button = binding.delete
            delete.setOnClickListener {
                realCommand.elseExists = false
                container.removeView(this)
                realCommand.insideElseBlock.clear()
            }

            innerContainer.addView(op as View)
            innerContainer.setViewDraggable(op, op)

            realCommand.insideElseBlock.add(op.command)
            true
        }

        addCommand.setOnClickListener {
            popupMenuElse.show()
        }

        accessory = list
    }
}