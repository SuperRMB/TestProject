package com.tiger.testproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhanghe on 2018/8/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(geteLayoutId());
        initData();
    }


    protected abstract int geteLayoutId();

    protected abstract void initData();
}
