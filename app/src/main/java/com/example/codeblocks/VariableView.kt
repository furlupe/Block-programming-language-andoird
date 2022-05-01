package com.example.codeblocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.example.codeblocks.databinding.VariableViewBinding
import java.security.KeyStore

class VariableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    private val binding = VariableViewBinding.inflate(LayoutInflater.from(context), this)

}