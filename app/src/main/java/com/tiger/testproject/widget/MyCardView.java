package com.tiger.testproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tiger.testproject.LogUtil;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class MyCardView extends ViewGroup {
    public MyCardView(Context context) {
        super(context);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;

            if (heightMode != MeasureSpec.EXACTLY){
                int count = getChildCount();
                float rotation = 5f;
                for (int i = 0; i < count; i++) {
                    View childAt = getChildAt(i);
                    measureChild(childAt,widthMeasureSpec,heightMeasureSpec);
                    childAt.setRotation(rotation);
                    int measuredHeight = childAt.getMeasuredHeight();
                    int measuredWidth = childAt.getMeasuredWidth();
                    double v = Math.tan(rotation * Math.PI / 180) * (measuredWidth / 2);
                    measuredHeight = (int) (measuredHeight + v *2);
                    height = Math.max(height,measuredHeight);
                    LogUtil.i(rotation+"=tan========"+v);
                    rotation += 5;
                }
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }


        LogUtil.i("====width====18236492101==="+width);
        LogUtil.i("====height======="+height);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        float rotation = 5f;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int cw = child.getMeasuredWidth();
            int ch = child.getMeasuredHeight();

            child.layout((measuredWidth-cw)/2, (measuredHeight - ch)/2,
                    (measuredWidth-cw)/2+cw, (measuredHeight - ch)/2+ch);
            child.setRotation(rotation);
            rotation += 5;
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    public class MarginLayoutParams extends LayoutParams{

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }


        public MarginLayoutParams(int width, int height) {
            super(width, height);
        }


        public MarginLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
