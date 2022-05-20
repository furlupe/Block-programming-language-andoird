package com.example.codeblocks.views.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.codeblocks.databinding.PrintViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Print

class PrintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = PrintViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = Print({ _, _ -> })
    override var accessory: MutableList<Command> = mutableListOf()

}