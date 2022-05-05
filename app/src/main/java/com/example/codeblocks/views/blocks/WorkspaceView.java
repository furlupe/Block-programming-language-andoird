package com.example.codeblocks.views.blocks;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WorkspaceView extends ConstraintLayout {

    private final ViewPoint mTemp = new ViewPoint();
    private final float mTouchSlop;
    private final Rect mBlocksBoundingBox = new Rect();

    public WorkspaceView(Context context) {
        this(context, null);
    }

    public WorkspaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkspaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTouchSlop = touchSlop;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        mBlocksBoundingBox.setEmpty();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
        }
    }

    @NonNull
    public Rect getBlocksBoundingBox(@NonNull Rect outRect) {
        outRect.set(mBlocksBoundingBox);
        return outRect;
    }
}
