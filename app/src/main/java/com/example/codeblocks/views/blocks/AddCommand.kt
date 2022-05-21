package com.example.codeblocks.views.blocks

import android.content.Context
import android.view.MenuItem
import com.example.codeblocks.R

fun addCommand(
    it: MenuItem,
    context: Context
): Block =
    when (it.itemId) {
        R.id.create_var -> CreateVariableView(context)
        R.id.assign_var -> AssignVariableView(context)
        R.id.if_block -> IfStartView(context)
        R.id.while_block -> WhileStartView(context)
        R.id.array_block -> ArrayView(context)
        R.id.print_block -> PrintView(context)
        R.id.input_block -> InputView(context)
        else -> throw Exception("wtf")
    }