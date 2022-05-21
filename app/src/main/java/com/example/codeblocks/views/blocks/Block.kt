package com.example.codeblocks.views.blocks

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.example.codeblocks.R
import com.example.codeblocks.model.Command
import com.example.codeblocks.model.Input
import com.jmedeisis.draglinearlayout.DragLinearLayout

interface Block {
    val binding: ViewBinding
    val command: Command
    var accessory: MutableList<Command>

    var prntFun: (toPrint: String, end: String) -> Unit
    var inputD: (command: Input) -> Unit

    fun init(container: DragLinearLayout, list: MutableList<Command>)

}