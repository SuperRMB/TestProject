package com.tiger.testproject.particle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhanghe on 2018/8/30.
 */

public class ParticleScene extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public static final int TIME_IN_FRAME = 30;
    private SurfaceHolder mHolder;
    private boolean mIsRunning;
    private Canvas mCanvas;
    private RectangleParticle mRectParticle;
    private RectangleParticle mRectParticle1;
    private RectangleParticle mRectParticle2;
    private RectangleParticle mRectParticle3;
    private Paint mPaint;

    public ParticleScene(Context context) {
        this(context, null);
    }

    public ParticleScene(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticleScene(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2f);

        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        int x = getDeviceWidth(getContext()) / 2;
        int x1 = 20;
        int x2 = 100;
        int x3 = 500;

        int y = 20;
        float[] startPosition = {x, y};
        float[] startPosition1 = {x1, y};
        float[] startPosition2 = {x2, y};
        float[] startPosition3 = {x3, y};

        mRectParticle = new RectangleParticle(startPosition);
        mRectParticle1 = new RectangleParticle(startPosition1);
        mRectParticle2 = new RectangleParticle(startPosition2);
        mRectParticle3 = new RectangleParticle(startPosition3);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
        new Thread(this).start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
    }


    @Override
    public void run() {

        while (mIsRunning) {
            /**取得更新之前的时间**/
            long startTime = System.currentTimeMillis();

            /**在这里加上线程安全锁**/
            synchronized (mHolder) {
                draw();
            }

            /**取得更新结束的时间**/
            long endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            int diffTime = (int) (endTime - startTime);

            /**确保每次更新时间为30帧**/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();
            }

        }
    }

    private void draw() {
        try {
            /**拿到当前画布 然后锁定**/
            mCanvas = mHolder.lockCanvas();//获取Canvas对象进行绘制
            //SurfaceView背景
            mPaint.setColor(mRectParticle.getColor());
            mCanvas.drawColor(Color.BLACK);
            Path particleShape = mRectParticle.getParticleShape();
            if (particleShape != null) {
                mCanvas.drawPath(particleShape, mPaint);
            }
//            mCanvas.drawPath(mRectParticle1.getParticleShape(),mPaint);
//            mCanvas.drawPath(mRectParticle2.getParticleShape(),mPaint);
//            mCanvas.drawPath(mRectParticle3.getParticleShape(),mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                /**绘制结束后解锁显示在屏幕上**/
                mHolder.unlockCanvasAndPost(mCanvas);//保证绘制的画布内容提交
            }
        }
    }


        /**
         * 屏幕宽
         *
         * @param context
         * @return
         */
    public static int getDeviceWidth(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    /**
     * 屏幕高
     *
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

}
