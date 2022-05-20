package com.example.codeblocks.views.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.codeblocks.databinding.CreateVariableViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Variable

class CreateVariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = CreateVariableViewBinding.inflate(LayoutInflater.from(context), this)
    override var command: Command = Variable("")
    override var accessory: MutableList<Command> = mutableListOf()
}