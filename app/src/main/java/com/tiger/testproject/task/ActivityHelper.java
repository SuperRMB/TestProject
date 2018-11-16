package com.tiger.testproject.task;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

/**
 * Created by zhanghe on 2018/8/29.
 */

public class ActivityHelper implements Application.ActivityLifecycleCallbacks {

    private static Stack<Activity> mActivityStack;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mActivityStack == null){
            mActivityStack = new Stack<>();
        }

        mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
        printAllActivity();
    }

    public void printAllActivity() {
        if (mActivityStack == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            Log.e("TAG", "位置" + i + ": " + mActivityStack.get(i));
        }
    }
}
