package com.example.codeblocks.views.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.codeblocks.databinding.WhileStartViewBinding

class WhileStartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){

    val binding = WhileStartViewBinding.inflate(LayoutInflater.from(context), this)

}