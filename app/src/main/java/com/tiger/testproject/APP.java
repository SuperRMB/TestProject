package com.tiger.testproject;

import android.app.Application;

import com.tiger.testproject.task.ActivityHelper;

/**
 * Created by zhanghe on 2018/8/29.
 */

public class APP extends Application {

    private ActivityHelper activityHelper;

    public static APP mAPP;

    public static ActivityHelper getActivityHelper() {
        return mAPP.activityHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAPP = this;
        activityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(activityHelper);
    }
}
