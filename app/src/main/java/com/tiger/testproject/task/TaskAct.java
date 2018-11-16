package com.tiger.testproject.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tiger.testproject.APP;

/**
 * Created by zhanghe on 2018/8/29.
 */

public class TaskAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("TaskAct1");
        setContentView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskAct.this, TaskAct2.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.getActivityHelper().printAllActivity();
    }

    @Override
    public void finish() {
        super.finish();
        Log.e("TAG", "finish后   TaskAct");
        APP.getActivityHelper().printAllActivity();
    }
}
