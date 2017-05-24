package me.texy.tableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;

/**
 * Created by xinyuanzhong on 2017/5/12.
 * <p>
 * A scroll view can scroll vertical and horizontal.
 */

public class StraightScrollView extends FrameLayout {

    private EdgeEffect mEdgeGlowLeft;
    private EdgeEffect mEdgeGlowRight;
    private EdgeEffect mEdgeGlowTop;
    private EdgeEffect mEdgeGlowBottom;

    private boolean mFillViewport;
    public StraightScrollView(@NonNull Context context) {
        this(context, null);
    }

    public StraightScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StraightScrollView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.HorizontalScrollView, defStyleAttr, 0);

        setFillViewport(a.getBoolean(R.styleable.HorizontalScrollView_fillViewport, false));

        a.recycle();
    }

    private void initScrollView() {

    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StraightScrollView can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StraightScrollView can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StraightScrollView can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StraightScrollView can host only one direct child");
        }

        super.addView(child, index, params);
    }

    /**
     * Indicates whether this HorizontalScrollView's content is stretched to
     * fill the viewport.
     *
     * @return True if the content fills the viewport, false otherwise.
     * @attr ref android.R.styleable#HorizontalScrollView_fillViewport
     */
    public boolean isFillViewport() {
        return mFillViewport;
    }

    /**
     * Indicates this HorizontalScrollView whether it should stretch its content width
     * to fill the viewport or not.
     *
     * @param fillViewport True to stretch the content's width to the viewport's
     *                     boundaries, false otherwise.
     * @attr ref android.R.styleable#HorizontalScrollView_fillViewport
     */
    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != mFillViewport) {
            mFillViewport = fillViewport;
            requestLayout();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mEdgeGlowLeft != null) {
            final int scrollX = getScrollX();
            if (!mEdgeGlowLeft.isFinished()) {
                final int restoreCount = canvas.save();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(270);
                canvas.translate(-height + getPaddingTop(), Math.min(0, scrollX));
                mEdgeGlowLeft.setSize(height, getWidth());
                if (mEdgeGlowLeft.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!mEdgeGlowRight.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(90);
                canvas.translate(-getPaddingTop(),
                        -(Math.max(getScrollRange(), scrollX) + width));
                mEdgeGlowRight.setSize(height, width);
                if (mEdgeGlowRight.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
        if (mEdgeGlowTop != null) {
            final int scrollY = getScrollY();
            boolean clipToPadding = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                clipToPadding = getClipToPadding();
            }
            if (!mEdgeGlowTop.isFinished()) {
                final int restoreCount = canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = getWidth() - getPaddingLeft() - getPaddingRight();
                    height = getHeight() - getPaddingTop() - getPaddingBottom();
                    translateX = getPaddingLeft();
                    translateY = getPaddingTop();
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate(translateX, Math.min(0, scrollY) + translateY);
                mEdgeGlowTop.setSize(width, height);
                if (mEdgeGlowTop.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!mEdgeGlowBottom.isFinished()) {
                final int restoreCount = canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = getWidth() - getPaddingLeft() - getPaddingRight();
                    height = getHeight() - getPaddingTop() - getPaddingBottom();
                    translateX = getPaddingLeft();
                    translateY = getPaddingTop();
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate(-width + translateX,
                        Math.max(getScrollRange(), scrollY) + height + translateY);
                canvas.rotate(180, width, 0);
                mEdgeGlowBottom.setSize(width, height);
                if (mEdgeGlowBottom.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }
}
