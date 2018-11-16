package com.tiger.testproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhanghe on 2018/8/23.
 */

public class DraggerView extends View {

    private Paint mPaint;

    public DraggerView(Context context) {
        this(context,null);
    }


    public DraggerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DraggerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        RectF rectF = new RectF(0,0,200,200);
        canvas.drawRect(rectF,mPaint);

        Path path = new Path();
        path.moveTo(0,100);
        path.quadTo(0,0,100,0);
        path.quadTo(200,0,200,100);
        path.quadTo(200,200,100,200);
        path.quadTo(0,200,0,100);
//        path.close();

        mPaint.setColor(Color.BLUE);
        canvas.drawPath(path,mPaint);
    }
}
