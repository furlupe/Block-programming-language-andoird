package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.codeblocks.databinding.WhileEndViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.example.codeblocks.model.While
import com.jmedeisis.draglinearlayout.DragLinearLayout

class WhileEndView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = WhileEndViewBinding.inflate(LayoutInflater.from(context), this)
    override var command: Command = While("")
    override var accessory: MutableList<Command> = mutableListOf()

    override var prntFun: (toPrint: String, end: String) -> Unit
        get() = TODO("Not yet implemented")
        set(value) {}
    override lateinit var inputD: (command: Input) -> Unit

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {

    }
}