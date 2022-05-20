package com.example.codeblocks.views.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocks.databinding.IfElseViewBinding
import com.example.codeblocks.databinding.IfStartViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.If

class IfStartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block{

    override val binding = IfStartViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = If("")
    override var accessory: MutableList<Command> = mutableListOf()

}