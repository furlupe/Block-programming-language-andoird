package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.codeblocks.databinding.IfElseViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.If
import com.example.codeblocks.model.Input
import com.jmedeisis.draglinearlayout.DragLinearLayout

class IfEndView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = IfElseViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = If("")
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var inputD: (command: Input) -> Unit

    override var prntFun: (toPrint: String, end: String) -> Unit
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {
        TODO("Not yet implemented")
    }
}