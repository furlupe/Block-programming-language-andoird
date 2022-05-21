package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.example.codeblocks.databinding.CreateVariableViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.example.codeblocks.model.Variable
import com.jmedeisis.draglinearlayout.DragLinearLayout

class CreateVariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = CreateVariableViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = Variable("")
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var prntFun: (toPrint: String, end: String) -> Unit
    override var inputD: (command: Input) -> Unit
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {

        val delete: Button = binding.delete
        delete.setOnClickListener {
            container.removeView(this)
            list.remove(this.command)
        }

        binding.variableName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                command.name = binding.variableName.text.toString()
            }

        })

        binding.variableValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                command.value = binding.variableValue.text.toString()
            }

        })

        accessory = list
        command.pos = container.indexOfChild(this)

    }

}