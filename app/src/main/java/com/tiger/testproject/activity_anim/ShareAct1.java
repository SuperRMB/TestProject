package com.tiger.testproject.activity_anim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tiger.testproject.R;

/**
 * Created by zhanghe on 2018/9/4.
 */

public class ShareAct1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareact1);

        final RecyclerView recy_view = findViewById(R.id.recy_view);
        recy_view.setLayoutManager(new LinearLayoutManager(this));
        ListShareAdapter listShareAdapter = new ListShareAdapter(this);
        recy_view.setAdapter(listShareAdapter);


        /*iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(ShareAct1.this, ShareAct2.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ShareAct1.this,
                            Pair.create(iv_share, "share"),
                            Pair.create(iv_share, "share_anim"))
                            .toBundle());
                }
            }
        });*/
    }
}
