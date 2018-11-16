package com.tiger.testproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class MyRelativeLayout extends RelativeLayout {
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width_size = MeasureSpec.getSize(widthMeasureSpec);

        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);

        /*LogUtil.i("=======MyRelativeLayout========");
        LogUtil.i("=width_mode=============="+LogUtil.modeSpec(width_mode));
        LogUtil.i("=width_size=============="+width_size);

        LogUtil.i("=height_mode=============="+LogUtil.modeSpec(height_mode));
        LogUtil.i("=height_size=============="+height_size);*/

//        ListView
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        /*LogUtil.i("=MyRelativeLayout======onAttachedToWindow========");
        LogUtil.i("=measuredWidth=============="+measuredWidth);
        LogUtil.i("=measuredHeight=============="+measuredHeight);*/
    }
}
