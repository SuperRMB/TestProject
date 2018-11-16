package com.tiger.testproject;

import android.util.Log;
import android.view.View;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class LogUtil {

    public static void i(String content){
        Log.i("zhang",content);
    }


    public static String modeSpec(int mode){
        String str = "未知";
        switch (mode){
            case View.MeasureSpec.AT_MOST:
                str = "AT_MOST";
                break;
            case View.MeasureSpec.EXACTLY:
                str = "EXACTLY";
                break;
            case View.MeasureSpec.UNSPECIFIED:
                str = "UNSPECIFIED";
                break;
        }
        return str;
    }
}
