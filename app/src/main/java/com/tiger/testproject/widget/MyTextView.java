package com.tiger.testproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.tiger.testproject.LogUtil;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {


    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width_size = MeasureSpec.getSize(widthMeasureSpec);

        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);

        LogUtil.i("=======MyTextView========");
        LogUtil.i("=width_mode=============="+LogUtil.modeSpec(width_mode));
        LogUtil.i("=width_size=============="+width_size);

        LogUtil.i("=height_mode=============="+LogUtil.modeSpec(height_mode));
        LogUtil.i("=height_size=============="+height_size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        LogUtil.i("=MyTextView======onDraw========");
        LogUtil.i("=measuredWidth=============="+measuredWidth);
        LogUtil.i("=measuredHeight=============="+measuredHeight);


        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int compoundPaddingLeft = getCompoundPaddingLeft();
        int compoundPaddingRight = getCompoundPaddingRight();
        int compoundPaddingTop = getCompoundPaddingTop();
        int compoundPaddingBottom = getCompoundPaddingBottom();

        int width = getWidth();
        int height = getHeight();


        int right = getRight();

        int left = getLeft();

        int top = getTop();

        int bottom = getBottom();

        int totalPaddingTop = getTotalPaddingTop();

        int totalPaddingBottom = getTotalPaddingBottom();

        int lineCount = getLayout().getLineCount();
        for (int i = 0; i < lineCount; i++) {
            float lineWidth = getLayout().getLineWidth(i);
        }
    }
}
