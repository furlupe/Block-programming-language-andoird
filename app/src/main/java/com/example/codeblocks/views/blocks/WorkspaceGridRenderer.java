package com.example.codeblocks.views.blocks;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

public class WorkspaceGridRenderer {
    public static final int DEFAULT_GRID_SPACING = 48;
    public static final int DEFAULT_GRID_COLOR = 0xffa0a0a0;
    public static final int DEFAULT_GRID_RADIUS = 2;

    private final Paint mGridPaint = new Paint();
    private final Paint mCirclePaint = new Paint();
    private final Rect mTempRect = new Rect();
    private Bitmap mGridBitmap;

    private int mGridSpacing = DEFAULT_GRID_SPACING;
    private int mGridRadius = DEFAULT_GRID_RADIUS;

    WorkspaceGridRenderer() {
        mCirclePaint.setColor(DEFAULT_GRID_COLOR);
    }

    int getGridSpacing() {
        return mGridSpacing;
    }

    void setGridSpacing(int mGridSpacing) {
        this.mGridSpacing = mGridSpacing;
    }

    void setGridColor(int gridColor) {
        mCirclePaint.setColor(gridColor);
    }

    void setGridDotRadius(int gridDotRadius) {
        mGridRadius = gridDotRadius;
    }

    void updateGridBitmap(float viewScale) {
        int gridSpacing = (int) (mGridSpacing * viewScale);

        // For some reason, reusing the same Bitmap via Bitmap.reconfigure() leads to bad rendering,
        // so recycle existing Bitmap and create a new one instead.
        if (mGridBitmap != null) {
            mGridBitmap.recycle();
        }
        mGridBitmap = Bitmap.createBitmap(gridSpacing, gridSpacing, Bitmap.Config.ARGB_8888);

        Canvas bitmapCanvas = new Canvas(mGridBitmap);
        bitmapCanvas.drawCircle(mGridRadius, mGridRadius, mGridRadius, mCirclePaint);

        mGridPaint.setShader(
                new BitmapShader(mGridBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    void drawGrid(Canvas canvas, int width, int height, int offsetX, int offsetY) {
        mTempRect.set(offsetX - mGridRadius, offsetY - mGridRadius,
                width + offsetX, height + offsetY);
        canvas.drawRect(mTempRect, mGridPaint);
    }
}
