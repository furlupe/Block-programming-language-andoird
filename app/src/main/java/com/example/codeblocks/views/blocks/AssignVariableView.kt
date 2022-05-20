package com.example.codeblocks.views.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocks.databinding.AssignVariableViewBinding
import com.example.codeblocks.model.Assign
import com.example.codeblocks.model.Command

class AssignVariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block{

    override val binding = AssignVariableViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = Assign("", "")
    override var accessory: MutableList<Command> = mutableListOf()
}