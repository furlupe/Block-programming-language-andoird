package com.example.codeblocks.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.View.OnDragListener
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.codeblocks.R

class TrashCanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(*[STATE_DEFAULT, STATE_ON_HOVER])
    annotation class HoverState

    protected var mState = STATE_DEFAULT
    protected var mDefaultDrawable: Drawable? = null
    protected var mOnHoverDrawable: Drawable? = null

    override fun setOnDragListener(dragListener: OnDragListener) {
        val wrapper = OnDragListener { view, dragEvent ->
            val action = dragEvent.action
            // Whether the dragged object can be handled by the trash.
            val result = dragListener.onDrag(view, dragEvent)
            if (action == DragEvent.ACTION_DRAG_ENDED) {
                setState(STATE_DEFAULT)
            } else if (result) {
                when (action) {
                    DragEvent.ACTION_DRAG_ENTERED -> setState(STATE_ON_HOVER)
                    DragEvent.ACTION_DROP, DragEvent.ACTION_DRAG_EXITED -> setState(STATE_DEFAULT)
                }
            }
            result
        }
        super.setOnDragListener(wrapper)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setState(STATE_DEFAULT)
    }

    private fun setDefaultIcon(drawableRes: Int) {
        setDefaultIcon(ContextCompat.getDrawable(context, drawableRes))
    }

    private fun setDefaultIcon(drawable: Drawable?) {
        mDefaultDrawable = drawable
        if (mState == STATE_DEFAULT) {
            setImageDrawable(mDefaultDrawable)
        }
    }

    private fun setOnHoverIcon(drawableRes: Int) {
        setOnHoverIcon(ContextCompat.getDrawable(context, drawableRes))
    }

    private fun setOnHoverIcon(drawable: Drawable?) {
        mOnHoverDrawable = drawable
        if (mState == STATE_ON_HOVER) {
            setImageDrawable(mOnHoverDrawable)
        }
    }

    private fun setState(state: Int) {
        mState = state
        when (state) {
            STATE_DEFAULT -> setImageDrawable(mDefaultDrawable)
            STATE_ON_HOVER -> setImageDrawable(mOnHoverDrawable)
            else -> {
                Log.w(TAG, "Invalid state: $state")
                setImageDrawable(mDefaultDrawable)
            }
        }
    }

    companion object {
        private const val TAG = "TrashCanView"
        private const val STATE_DEFAULT = 0
        private const val STATE_ON_HOVER = 1
    }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TrashCanView,
            0, 0
        )
        try {
            setDefaultIcon(
                a.getResourceId(
                    R.styleable.TrashCanView_defaultIcon, R.drawable.blockly_trash
                )
            )
            setOnHoverIcon(
                a.getResourceId(
                    R.styleable.TrashCanView_onHoverIcon, R.drawable.blockly_trash_open
                )
            )
        } finally {
            a.recycle()
        }
    }
}