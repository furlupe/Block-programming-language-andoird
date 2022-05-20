package com.example.codeblocks.views.blocks

import androidx.viewbinding.ViewBinding
import com.example.codeblocks.model.Command

interface Block{
    val binding: ViewBinding
    val command: Command
    var accessory: MutableList<Command>
}