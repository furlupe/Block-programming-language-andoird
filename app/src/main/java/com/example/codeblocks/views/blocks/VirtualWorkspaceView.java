package com.example.codeblocks.views.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.codeblocks.R;


public class VirtualWorkspaceView extends ConstraintLayout {
    private static final String TAG = "VirtualWorkspaceView";
    private static final boolean DEBUG = false;

    private static final int DESIRED_WIDTH = 2048;
    private static final int DESIRED_HEIGHT = 2048;

    private static final float MIN_SCALE_TO_DRAW_GRID = 0.5f;

    private static final float[] ZOOM_SCALES = new float[]{0.25f, 0.5f, 1.0f, 2.0f};
    private static final int INIT_ZOOM_SCALES_INDEX = 2;

    protected boolean mScrollable = true;
    protected boolean mScalable = true;

    private final ViewPoint mPanningStart = new ViewPoint();

    private final WorkspaceGridRenderer mGridRenderer = new WorkspaceGridRenderer();

    private int mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
    private int mOriginalScrollX;
    private int mOriginalScrollY;
    private int mCurrentZoomScaleIndex = INIT_ZOOM_SCALES_INDEX;
    private float mViewScale = ZOOM_SCALES[INIT_ZOOM_SCALES_INDEX];

    private boolean mDrawGrid = true;

    private WorkspaceView mWorkspaceView;
    private boolean mResetViewPending = true;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mTapGestureDetector;
    private InputMethodManager mImeManager;

    private Rect mTempRect = new Rect();

    public VirtualWorkspaceView(Context context) {
        this(context, null);
    }

    public VirtualWorkspaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirtualWorkspaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        mWorkspaceView = (WorkspaceView) findViewById(R.id.workspace);
        mWorkspaceView.setPivotX(0);
        mWorkspaceView.setPivotY(0);

        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(mScrollable);
        setVerticalScrollBarEnabled(mScrollable);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());
        mTapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());
        mGridRenderer.updateGridBitmap(mViewScale);
        mImeManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void postResetView() {
        mResetViewPending = true;
    }

    public void resetView() {
        mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
        mPanningStart.set(0, 0);
        mOriginalScrollX = 0;
        mOriginalScrollY = 0;

        updateScaleStep(INIT_ZOOM_SCALES_INDEX);
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    public void setDrawGrid(boolean drawGrid) {
        mDrawGrid = drawGrid;
    }

    protected void setScrollable(boolean scrollable) {
        if (scrollable == mScrollable) {
            return;
        }
        mScrollable = scrollable;
        setHorizontalScrollBarEnabled(mScrollable);
        setVerticalScrollBarEnabled(mScrollable);
        if (!mScrollable) {
            resetView();
        }
    }

    protected void setScalable(boolean scalable) {
        if (mScalable == scalable) {
            return;
        }
        mScalable = scalable;
        if (!scalable) {
            resetView();
        }
    }

    public boolean zoomIn() {
        if (mCurrentZoomScaleIndex < ZOOM_SCALES.length - 1) {
            updateScaleStep(mCurrentZoomScaleIndex + 1);
            return true;
        }
        return false;
    }

    public boolean zoomOut() {
        if (mScrollable && mCurrentZoomScaleIndex > 0) {
            updateScaleStep(mCurrentZoomScaleIndex - 1);
            return true;
        }
        return false;
    }

    public float getViewScale() {
        return mViewScale;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mTapGestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScalable && mScaleGestureDetector != null) {
            mScaleGestureDetector.onTouchEvent(event);
            if (mScaleGestureDetector.isInProgress()) {
                return true;
            }
        }

        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                clearFocus();
                mImeManager.hideSoftInputFromWindow(getWindowToken(), 0);

                if (mScrollable) {
                    final int pointerIdx = event.getActionIndex();
                    mPanningPointerId = event.getPointerId(pointerIdx);
                    mPanningStart.set(
                            (int) event.getX(pointerIdx),
                            (int) event.getY(pointerIdx));
                    mOriginalScrollX = getScrollX();
                    mOriginalScrollY = getScrollY();
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mPanningPointerId != MotionEvent.INVALID_POINTER_ID) {
                    final int pointerIdx =
                            event.findPointerIndex(mPanningPointerId);
                    if (pointerIdx == -1) {
                        Log.w(TAG, "Got an invalid pointer idx for the panning pointer.");
                        return false;
                    }
                    scrollTo(
                            mOriginalScrollX + mPanningStart.x -
                                    (int) event.getX(pointerIdx),
                            mOriginalScrollY + mPanningStart.y -
                                    (int) event.getY(pointerIdx));
                    return true;
                } else {
                    return false;
                }
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIdx = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIdx);
                if (pointerId != mPanningPointerId) {
                    return false;
                }
            }
            case MotionEvent.ACTION_UP: {
                if (mPanningPointerId != MotionEvent.INVALID_POINTER_ID) {
                    mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
                    return true;
                } else {
                    return false;
                }
            }
            case MotionEvent.ACTION_CANCEL: {
                if (mPanningPointerId != MotionEvent.INVALID_POINTER_ID) {
                    scrollTo(mOriginalScrollX, mOriginalScrollY);
                    mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
                    return true;
                } else {
                    return false;
                }
            }
            default: {
                break;
            }
        }

        return false;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getMeasuredSize(widthMeasureSpec, DESIRED_WIDTH),
                getMeasuredSize(heightMeasureSpec, DESIRED_HEIGHT));

        mWorkspaceView.measure(
                View.MeasureSpec.makeMeasureSpec(
                        (int) (getMeasuredWidth() / mViewScale), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(
                        (int) (getMeasuredHeight() / mViewScale), View.MeasureSpec.EXACTLY));
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mResetViewPending) {
            resetView();
            mResetViewPending = false;
        }

        final int offsetX = getScrollX();
        final int offsetY = getScrollY();
        mWorkspaceView.layout((offsetX), (offsetY),
                (int) ((getMeasuredWidth() / mViewScale) + offsetX),
                (int) ((getMeasuredHeight() / mViewScale) + offsetY));
    }

    @Override
    protected void onDraw(Canvas c) {
        if (shouldDrawGrid()) {
            mGridRenderer.drawGrid(c, getWidth(), getHeight(), getScrollX(), getScrollY());
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (!mScrollable) {
            return;
        }

        Rect blocksBounds = getViewScaledBlockBounds();
        int blocksWidth = blocksBounds.width();
        int blocksHeight = blocksBounds.height();

        int viewWidth = getMeasuredWidth();
        int halfViewWidth = viewWidth / 2;
        int viewHeight = getMeasuredHeight();
        int halfViewHeight = viewHeight / 2;

        int horzMargin = halfViewWidth;
        if (blocksWidth < halfViewWidth) {
            horzMargin = viewWidth - blocksWidth;
        }

        int vertMargin = halfViewHeight;
        if (blocksHeight < halfViewHeight) {
            vertMargin = viewHeight - blocksHeight;
        }

        final int xMin = blocksBounds.left - horzMargin;
        final int xMax = blocksBounds.right + horzMargin - viewWidth;
        x = clampToRange(x, xMin, xMax);

        final int yMin = blocksBounds.top - vertMargin;
        final int yMax = blocksBounds.bottom + vertMargin - viewHeight;
        y = clampToRange(y, yMin, yMax);

        super.scrollTo(x, y);
    }

    private static int getMeasuredSize(int measureSpec, int desiredSize) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);

        if (mode == View.MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == View.MeasureSpec.AT_MOST) {
            return Math.min(size, desiredSize);
        } else {
            return desiredSize;
        }

    }

    @Override
    protected int computeHorizontalScrollRange() {
        final Rect viewScaledBlockBounds = getViewScaledBlockBounds();
        final int viewScaledWorkspaceRange =
                viewScaledBlockBounds.right - viewScaledBlockBounds.left;
        final int width = getMeasuredWidth();
        int totalMargin = width;  // By default, leave a half screen width on left and right.
        if (viewScaledWorkspaceRange < width / 2) {
            // Make sure blocks can touch each edge.
            totalMargin = 2 * (width - viewScaledWorkspaceRange);
        }
        return viewScaledWorkspaceRange + totalMargin;
    }

    @Override
    protected int computeHorizontalScrollExtent() {
        return getMeasuredWidth();
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        final Rect viewScaledBlockBounds = getViewScaledBlockBounds();
        return getScrollX() - (viewScaledBlockBounds.left - computeHorizontalScrollExtent()) / 2;
    }

    @Override
    protected int computeVerticalScrollRange() {
        final Rect viewScaledBlockBounds = getViewScaledBlockBounds();
        final int viewScaledWorkspaceRange =
                viewScaledBlockBounds.bottom - viewScaledBlockBounds.top;
        final int height = getMeasuredHeight();
        int totalMargin = height;  // By default, leave a half screen height on top and bottom.
        if (viewScaledWorkspaceRange < height / 2) {
            // Make sure blocks can touch each edge.
            totalMargin = 2 * (height - viewScaledWorkspaceRange);
        }
        return viewScaledWorkspaceRange + totalMargin;
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return getMeasuredHeight();
    }

    @Override
    protected int computeVerticalScrollOffset() {
        final Rect viewScaledBlockBounds = getViewScaledBlockBounds();
        return getScrollY() - (viewScaledBlockBounds.top - computeVerticalScrollExtent()) / 2;
    }

    private void updateScaleStep(int newScaleIndex) {
        if (newScaleIndex != mCurrentZoomScaleIndex) {
            final float oldViewScale = mViewScale;

            mCurrentZoomScaleIndex = newScaleIndex;
            mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];

            final float scaleDifference = mViewScale - oldViewScale;
            scrollBy((int) (scaleDifference * getMeasuredWidth() / 2),
                    (int) (scaleDifference * getMeasuredHeight() / 2));

            if (shouldDrawGrid()) {
                mGridRenderer.updateGridBitmap(mViewScale);
            }

            mWorkspaceView.setScaleX(mViewScale);
            mWorkspaceView.setScaleY(mViewScale);
            mWorkspaceView.requestLayout();
        }
    }

    private boolean shouldDrawGrid() {
        return mDrawGrid && mViewScale >= MIN_SCALE_TO_DRAW_GRID;
    }

    private static int clampToRange(int y, int min, int max) {
        return Math.min(max, Math.max(min, y));
    }

    public int getGridSpacing() {
        return mGridRenderer.getGridSpacing();
    }

    public void setGridSpacing(int gridSpacing) {
        mGridRenderer.setGridSpacing(gridSpacing);
    }

    public void setGridColor(int gridColor) {
        mGridRenderer.setGridColor(gridColor);
    }

    public void setGridDotRadius(int gridDotRadius) {
        mGridRenderer.setGridDotRadius(gridDotRadius);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onSingleTapUp(MotionEvent e) {
            return callOnClick();
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mStartFocusX;
        private float mStartFocusY;
        private float mStartScale;
        private int mStartScrollX;
        private int mStartScrollY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mStartFocusX = detector.getFocusX();
            mStartFocusY = detector.getFocusY();

            mStartScrollX = getScrollX();
            mStartScrollY = getScrollY();

            mStartScale = mViewScale;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            final float oldViewScale = mViewScale;

            final float scaleFactor = detector.getScaleFactor();
            mViewScale *= scaleFactor;

            if (mViewScale < ZOOM_SCALES[0]) {
                mCurrentZoomScaleIndex = 0;
                mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];
            } else if (mViewScale > ZOOM_SCALES[ZOOM_SCALES.length - 1]) {
                mCurrentZoomScaleIndex = ZOOM_SCALES.length - 1;
                mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];
            } else {
                float minDist = Float.MAX_VALUE;
                int index = ZOOM_SCALES.length - 1;
                for (int i = 0; i < ZOOM_SCALES.length; i++) {
                    float dist = Math.abs(mViewScale - ZOOM_SCALES[i]);
                    if (dist < minDist) {
                        minDist = dist;
                    } else {
                        index = i - 1;
                        break;
                    }
                }
                mCurrentZoomScaleIndex = index;
            }

            if (shouldDrawGrid()) {
                mGridRenderer.updateGridBitmap(mViewScale);
            }

            mWorkspaceView.setScaleX(mViewScale);
            mWorkspaceView.setScaleY(mViewScale);

            final float scaleDifference = mViewScale - mStartScale;
            final int scrollScaleX = (int) (scaleDifference * mStartFocusX);
            final int scrollScaleY = (int) (scaleDifference * mStartFocusY);

            final int scrollPanX = (int) (mStartFocusX - detector.getFocusX());
            final int scrollPanY = (int) (mStartFocusY - detector.getFocusY());

            scrollTo(mStartScrollX + scrollScaleX + scrollPanX,
                    mStartScrollY + scrollScaleY + scrollPanY);

            return true;
        }
    }

    @NonNull
    private Rect getViewScaledBlockBounds() {
        mWorkspaceView.getBlocksBoundingBox(mTempRect);
        mTempRect.left = (int) Math.floor(mTempRect.left * mViewScale);
        mTempRect.right = (int) Math.ceil(mTempRect.right * mViewScale);
        mTempRect.top = (int) Math.floor(mTempRect.top * mViewScale);
        mTempRect.bottom = (int) Math.ceil(mTempRect.bottom * mViewScale);
        return mTempRect;
    }
}
