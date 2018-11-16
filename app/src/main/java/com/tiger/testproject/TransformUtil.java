package com.tiger.testproject;

import android.content.Context;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class TransformUtil {

    private static int DRAWING_WIDTH = 720;
    private static int DRAWING_HEIGHT = 1280;

    /**
     * 根据720图计算真实宽高比例
     * @param context
     * @param width
     * @param height
     * @return realWH[0] 宽    realWH[1] 高
     */
    public static int[] countRealWH(Context context,int width,int height){
        int deviceWidth = getDeviceWidth(context);
        int deviceHeight = getDeviceHeight(context);
        int[] realWH = new int[2];
        float wp = width / DRAWING_WIDTH;
        float real_w = wp * deviceWidth;
        float real_h = height * deviceHeight / DRAWING_HEIGHT;
        realWH[0] = (int) (real_w + 0.5f);
        if (width == height){
            realWH[1] = (int) (real_w + 0.5f);
        }else {
            realWH[1] = (int) (real_h + 0.5f);
        }
        return realWH;
    }

    public static int getDeviceHeight(Context context) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    public static int getDeviceWidth(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    /**
     * 计算真实宽
     * @param context
     * @param width 720*1280标注图上给的像素
     * @return
     */
    public static int countRealWidth(Context context, int width){
        float wp = width / DRAWING_WIDTH;
        float realW = wp * getDeviceWidth(context);
        return (int) (realW + 0.5f);
    }

    /**
     * 计算真实高
     * @param context
     * @param height 720*1280标注图上给的像素
     * @return
     */
    public static int countRealHeight(Context context,int height){
        float hp = height / DRAWING_HEIGHT;
        float realH = hp * getDeviceHeight(context);
        return (int) (realH + 0.5f);
    }

    /**
     * dip转换px
     *
     * @param context
     * @param dpValue
     * @return float
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
