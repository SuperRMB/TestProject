package com.tiger.testproject.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tiger.testproject.APP;

import java.util.Stack;

/**
 * Created by zhanghe on 2018/8/29.
 */

public class TaskAct2 extends AppCompatActivity {

    private Stack<String> stringStack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("TaskAct2");
        setContentView(textView);

        stringStack = new Stack<>();
        stringStack.add("1");
        stringStack.add("2");
        stringStack.add("3");
        stringStack.add("4");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TaskAct2.this, TaskAct3.class));
//                finish();
                stringStack.remove("2");

                for (int i = 0; i < stringStack.size(); i++) {
                    System.out.println(i+"=Stack========"+stringStack.get(i));
                }
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
