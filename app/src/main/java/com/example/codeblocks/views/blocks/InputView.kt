package com.example.codeblocks.views.blocks

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.codeblocks.R
import com.example.codeblocks.databinding.InputViewBinding
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.jmedeisis.draglinearlayout.DragLinearLayout

class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Block {

    override val binding = InputViewBinding.inflate(LayoutInflater.from(context), this)
    override val command = Input()
    override var accessory: MutableList<Command> = mutableListOf()

    override lateinit var prntFun: (toPrint: String, end: String) -> Unit
    override lateinit var inputD: (command: Input) -> Unit

    override fun init(container: DragLinearLayout, list: MutableList<Command>) {

        inputD(command)
        val delete: Button = binding.delete
        delete.setOnClickListener {
            container.removeView(this)
            list.remove(command)
        }

        binding.inputTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                command.toInput = binding.inputTo.text.toString()
            }

        })

        accessory = list
        command.pos = container.indexOfChild(this)
    }
}