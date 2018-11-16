package com.tiger.testproject.activity_anim;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.tiger.testproject.R;

/**
 * Created by zhanghe on 2018/9/4.
 */

public class ShareAct2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.shareact2);

        ViewPager vp = findViewById(R.id.vp);
        SharePageAdapter adapter = new SharePageAdapter(this);
        vp.setAdapter(adapter);
    }
}
