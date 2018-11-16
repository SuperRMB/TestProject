package com.tiger.testproject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zhanghe on 2018/8/28.
 */

public class UnitTestAct extends BaseActivity {

    private EditText et_test;
    private TextView tv_test;

    @Override
    protected int geteLayoutId() {
        return R.layout.unit_test_act;
    }

    @Override
    protected void initData() {

        tv_test = findViewById(R.id.tv_test);
        et_test = findViewById(R.id.et_test);
        Button btn_test = findViewById(R.id.btn_test);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String s = et_test.getText().toString();
        if ( s!= null ){
            tv_test.setText(s);
        }
    }


}
